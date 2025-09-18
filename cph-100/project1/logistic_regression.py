import numpy as np
import tqdm

class LogisticRegression():
    """
        A logistic regression model trained with stochastic gradient descent.
    """

    def __init__(self, num_epochs=100, learning_rate=1e-4, batch_size=16, regularization_lambda=0,  verbose=False):
        self.num_epochs = num_epochs
        self.learning_rate = learning_rate
        self.batch_size = batch_size
        self.verbose = verbose
        self.regularization_lambda = regularization_lambda

    def sigmoid(self, z):
        return np.where(z >= 0, 
            1 / (1 + np.exp(-z)), 
            np.exp(z) / (1 + np.exp(z)))

    def fit(self, X, Y):
        """
            Train the logistic regression model using stochastic gradient descent.
        """
        # Ensure X and Y are numpy numeric arrays
        X = np.asarray(X, dtype=float)
        Y = np.asarray(Y, dtype=float)

        n, d = X.shape
        theta = np.zeros(d)
        bias = 0.0
        for epoch in range(1, self.num_epochs + 1):
            if self.verbose:
                print("Starting epoch {}".format(epoch))
            # Shuffle the data at the start of each epoch
            indices = np.arange(n)
            np.random.shuffle(indices)
            X_shuffled = X[indices]
            Y_shuffled = Y[indices]

            for start in tqdm.tqdm(range(0, n, self.batch_size), disable=not self.verbose):
                end = min(start + self.batch_size, n)
                X_batch = X_shuffled[start:end]
                Y_batch = Y_shuffled[start:end]

                grad_theta, grad_bias = self.gradient(X_batch, Y_batch)

                # Update parameters
                theta -= self.learning_rate * grad_theta
                bias -= self.learning_rate * grad_bias

    def gradient(self, X, Y):
        """
            Compute the gradient of the loss with respect to theta and bias with L2 Regularization.
            Hint: Pay special attention to the numerical stability of your implementation.
        """
        m = X.shape[0]
        z = np.dot(X, self.theta) + self.bias
        preds = self.sigmoid(z)

        error = preds - Y
        grad_theta = (1/m) * X.T.dot(error) + (self.reg_lambda/m) * self.theta
        grad_bias  = (1/m) * np.sum(error)

        return grad_theta, grad_bias

    def predict_proba(self, X):
        """
            Predict the probability of lung cancer for each sample in X.
        """
        X = np.asarray(X, dtype=float)
        return self.sigmoid(X.dot(self.theta) + self.bias)

    def predict(self, X, threshold=0.5):
        """
            Predict the if patient will develop lung cancer for each sample in X.
        """
        return (self.predict_proba(X) >= threshold).astype(int)