# ML Fundamentals

## Feature Engineering

### Categorical data

E.g. professions

One hot encoding

can add more columns for intensity of a professsio

### Numerical Data

E.g. Age

Standardize age -> 73 -> .73

Problem introduced: Age is represented as a linear function, where age is more of a logorithmic, and decreases in "biological change" when you get older

### Ordinal data

Ex. cough severity

We can treat like categorical data, while including the weight of each severity

Low -> [0, 0, 1]
Mid -> [0, 1, 1]
High -> [1, 1, 1]

### Dealing with non-linear data

increase dimension n to make a feature linear

technically you can increase n forever,
however, not scalable

### Regularization

set a low loss function (plane) and set new constrints

then, new low loss function has to satisfy those constraints so that we can set more miniscule parameters

increase bias towards simplicity

### Normalization

if a feature vector has too high of a weight, then we can adjust the regularization (penalizes large weights) OR learning rate

## Cross Validation

Good if you have small dataset -> takes average variance

Bad if you want to reduce training costs
