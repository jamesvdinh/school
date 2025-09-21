import numpy as np


class Vectorizer:
    """
        Transform raw data into feature vectors. Support ordinal, numerical and categorical data.
        Also implements feature normalization and scaling.

        TODO: Support numerical, ordinal, categorical, histogram features.
    """

    def __init__(self, feature_config, num_bins=5):
        self.feature_config = feature_config
        self.feature_transforms = {}
        self.is_fit = False

    def get_numerical_vectorizer(self, values, verbose=False):
        """
        :return: function to map numerical x to a zero mean, unit std dev normalized score.
        """
        clean_values = [v for v in values if str(v).strip() != '']
        # If everything was missing, default to [0.0]
        if not clean_values:
            clean_values = [0.0]

        values = np.array(clean_values, dtype=float)
        mean, std = np.mean(values), np.std(values)

        def vectorizer(x):
            """
            :param x: numerical value
            Return transformed score

            Hint: this fn knows mean and std from the outer scope
            """
            try:
                x = float(x)
            except (ValueError, TypeError):
                x = 0.0  # fallback for missing/bad inputs

            x = float(x)
            if std == 0:
                return [0.0]
            return [(x - mean) / std]

        return vectorizer

    def get_histogram_vectorizer(self, values):
        """
        :return: function to map histogram x to a normalized histogram vector
        """
        bins = 5

        clean_values = [int(v) for v in values if str(v).strip() != ""]
        if not clean_values:
            raise ValueError(
                "No valid numeric values provided to histogram vectorizer")

        values = np.array(clean_values, dtype=float)
        bin_edges = np.linspace(values.min(), values.max(), bins + 1)

        def vectorizer(x):
            if str(x).strip() == "":
                return [0.0] * bins
            try:
                x = float(x)
            except ValueError:
                raise ValueError(f"Invalid input to histogram vectorizer: {x}")
            bin_idx = np.digitize(x, bin_edges, right=False) - 1
            bin_idx = max(0, min(bin_idx, bins - 1))
            one_hot = np.zeros(bins)
            one_hot[bin_idx] = 1.0
            return one_hot.tolist()

        return vectorizer

    def get_categorical_vectorizer(self, values):
        """
        :return: function to map categorical x to one-hot feature vector
        """
        categories = sorted(set(values))
        index_map = {cat: i for i, cat in enumerate(categories)}
        dim = len(categories)

        def vectorizor(x):
            one_hot = np.zeros(dim)
            if x in index_map:
                one_hot[index_map[x]] = 1.0
            return one_hot.tolist()

        return vectorizor

    def fit(self, X):
        """
            Leverage X to initialize all the feature vectorizers (e.g. compute means, std, etc)
            and store them in self.

            This implementation will depend on how you design your feature config.

            Steps:
                1. iterate each feature in feature_config
                2. extract all values for that feature from X (each row is a datapoint)
                3. store the appropriate get_*_vectorizer fn in self.feature_transforms
        """
        if self.feature_config is None:
            self.feature_transforms = {"transform_name": None}
            return

        # "numerical": ["age"]
        for feat_type, features in self.feature_config.items():
            for feat in features:  # "age" in ["age"]
                values = [x[feat] for x in X if x[feat] is not None]

                if feat_type == "numerical":
                    self.feature_transforms[feat] = self.get_numerical_vectorizer(
                        values)
                elif feat_type == "categorical":
                    self.feature_transforms[feat] = self.get_categorical_vectorizer(
                        values)
                elif feat_type == "ordinal":
                    self.feature_transforms[feat] = self.get_histogram_vectorizer(
                        values)
                else:
                    raise ValueError(f"Unknown feature type: {feat_type}")
        self.is_fit = True

    def transform(self, X):
        """
        For each data point, apply the feature transforms and concatenate the results into a single feature vector.

        :param X: list of dicts, each dict is a datapoint
        """

        if not self.is_fit:
            raise Exception(
                "Vectorizer not intialized! You must first call fit with a training set")

        transformed_data = []
        feature_order = list(self.feature_transforms.keys())
        for r in X:
            row = []
            for feat in feature_order:
                val = r.get(feat, None)
                if val is None:
                    row.append(0.0)
                else:
                    feat_val = self.feature_transforms[feat](val)
                    if isinstance(feat_val, (list, np.ndarray)):
                        # flatten multi-dimensional features
                        row.extend(feat_val)
                    else:
                        row.append(float(feat_val))
            transformed_data.append(row)

        return np.array(transformed_data, dtype=float)
