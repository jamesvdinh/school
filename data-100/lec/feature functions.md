# Feature Functions

- a function that transforms raw features to transformed features
  - Ex. tranforming lat and lon values to distance from a school.

## One-hot Encoding

- feature engineering technique to transform **qualatative** data into **numeric features**
  - eg. transform days of the week into binary 0, 1 to indicate which day the data point recorded

![alt text](/img/one-hot.png)

## Overfitting

- the model "memorizes" the training data
- does not do well on generalized data

### causes:

- adding too many parameters

### Decrease training error

- increase model complexity
- minimize model test error

![alt text](/img/image.png)
