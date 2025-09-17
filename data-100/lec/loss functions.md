# Minimizing loss functions

## Initial steps

1. Define a model

2. Choose loss function -> calculate average loss

3. Find best parameter $\hat{\theta}$ of $\theta$ that minimizes loss

   - there vcan be multiple $\theta$ values

4. Evaluate model performance

## Linear Model

> $$\large \hat{y} = \theta_0 + \theta_1x$$

## $L_2$ (squared loss)

> $$\large L_2(y, \hat{y}) = (y - \hat{y})^2 = (y - (\theta_0 + \theta_1x)^2$$

- squared loss
- for a single data point

### optimizing $\theta$ for a constant model

Optimal solution is always the mean of the actual values

## Mean Squared Error (MSE)

> $$\large R(\theta_0, \theta_1) = 1/n \sum_{i=1}^n L(y_i, \hat{y}_i)$$
>
> $$= \large \frac{1}{n} \sum_{i = 1}^n(y_i - (\theta_0 + \theta_1 x_i))^2$$

- uses $L_2$ to take the average loss across the dataset

## Best parameter $\hat{\theta}$

> ### _slope_ of fitted linear model
>
> $$\large \hat{\theta}_1 = r\dfrac{\sigma_y}{\sigma_x}$$
>
> ### _intercept_ of fitted linear model
>
> $$\large \hat{\theta}_0 = \bar{y} - \hat{\theta}_1\bar{x}$$
>
> ### _prediction_ for $x_i$:
>
> $$\large \hat{y}_i = \hat{\theta_0} + \hat{\theta_1}x_i$$

- $\bar{x}, \bar{y}$: x, y means
- $\sigma_x, \sigma_y$: x, y standard deviations
- $r$: correlation coefficient

## $L_1$ (absolute loss)

> $$\large L_1(y, \hat{y}) = |y - \hat{y}|$$

- absolute loss

### optimizing $\theta$ for a constant model

Optimal solution is always the median of the actual values

- since we are trying to find the balance with equal number of values above and below $\hat{y}$

## Mean Absolute Error (MAE)

> $$\large R\left(\theta_0,\theta_1\right) = \large \frac{1}{n} \sum_{i=1}^n L_1(y_i, \hat{y}_i) $$
> $$ = \large \frac{1}{n} \sum\_{i=1}^n |y_i - (\theta_0 + \theta_1 x)| $$
