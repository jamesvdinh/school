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
        values = np.array(values, dtype=float)
        mean, std = np.mean(values), np.std(values)

        def vectorizer(x):
            """
            :param x: numerical value
            Return transformed score

            Hint: this fn knows mean and std from the outer scope
            """
            x = float(x)
            if std == 0:
                return 0.0
            return (x - mean) / std

        return vectorizer

    def get_histogram_vectorizer(self, values):
        raise NotImplementedError("Histogram vectorizer not implemented yet")

    def get_categorical_vectorizer(self, values):
        """
        :return: function to map categorical x to one-hot feature vector
        """
        raise NotImplementedError("Categorical vectorizer not implemented yet")

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
            self.feature_transforms = { "transform_name": None}
            return
        
        for feat_type, features in self.feature_config.items(): # "numerical": ["age"]
            for feat in features: # "age" in ["age"]
                values = [x[feat] for x in X if x[feat] is not None]

                if feat_type == "numerical":
                    self.feature_transforms[feat] = self.get_numerical_vectorizer(values)
                elif feat_type == "categorical":
                    self.feature_transforms[feat] = self.get_categorical_vectorizer(values)
                elif feat_type == "histogram":
                    self.feature_transforms[feat] = self.get_histogram_vectorizer(values)
                else:
                    raise ValueError(f"Unknown feature type: {feat_type}")
        self.is_fit = True


    def transform(self, X):
        """
        For each data point, apply the feature transforms and concatenate the results into a single feature vector.

        :param X: list of dicts, each dict is a datapoint
        """

        if not self.is_fit:
            raise Exception("Vectorizer not intialized! You must first call fit with a training set" )

        transformed_data = []
        for r in X:
            new_row = {}
            for feat, transform in self.feature_transforms.items():
                new_row[feat] = transform(r[feat])
            transformed_data.append(new_row)
            
        return np.array(transformed_data)