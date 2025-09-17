# Linear Regression

- in sklearn, you must set `fit_intercept=False` to prevent linear dependence

## Simple Linear Regression (SLR)

- Regression Line: straight line that minimizes mean squared error of estimation

> $\large \text{slope} = r * \dfrac{\text{std of y}}{\text{std of x}}$
>
> $\large \text{y-intercept} = \bar{y} - slope * \bar{x}$
>
> $\large \text{regression estimate} = slope * x + y-intercept$
>
> $\large \text{residual} = \text{observed y} - \text{regression estimate}$

## Ordinary Linear Regression (OLR)

> $$\large y = \theta_0 + \theta_1x_1 + \theta_2x_2 + ... + \theta_nx_n$$

- multiple independent vars
- more complex

## Linearizing Graphs

Tranformations to make non-linear relationships appear linear on a visualization

### Over-achiever

> scale x-axis variable
>
> ![alt text](/img/overachiever.png)

### Under-achiever

> scale y-axis variable
>
> ![alt text](/img/underachiever.png)

# Multiple Regression

## Ordinary Least Squares (OLS)

> $$\large \hat{\theta} = (X^TX)^{-1}X^TY$$
