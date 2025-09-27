Initialization, BatchNorm only improve the training process, not completely fix it

## Activation Functions
Calculates the output of the node based on its individual inputs and weights
- Ex. Identity, binary step, logistic, sigmoid, hyperbolic tangent

![[Screenshot 2025-09-27 at 11.41.09 AM.png]]
## Batch Norm
a technique that normalizes the activations of a layer within a mini-batch during training
- prevents activation collapse
- apply normalization on outputs of hidden layers for input in next layer
- inserted between a hidden layer and next hidden layer

2 learnable parameters
- beta
- gamma
2 non-learnable parameters
- Mean moving avg
- Var moing avg

![Calculations performed by Batch Norm layer (Image by Author)](https://towardsdatascience.com/wp-content/uploads/2021/05/1VsN_9_AN2ji8hCZYSTTV0w.png)

## Optimizers
Problems with gradient descent (full-batch)
- Local minima: GD might settle at a local minima and might get stuck without ever reaching the global minima
![Local minima and Global minimum (Source)](https://towardsdatascience.com/wp-content/uploads/2021/04/1B5B28RUrgXbxdz-f-PHhRQ.png)

- Saddle points: in one direction for on parameter, the curve is at a local minimum, yet in a second direction (second parameter), the curve is at a local maximum
	- gradients are close to zero at saddle point
![Saddle Point (Source)](https://towardsdatascience.com/wp-content/uploads/2021/04/0YgSaU3ugLX3YRvRu.png)

- Ravines: long narrow valley that slopes steeply in one direction and gently in the second direction
	- makes it hard to lead to minimum because of its difficulty to navigate
![Ravines (Modified from Source, by permission of James Martens)](https://towardsdatascience.com/wp-content/uploads/2021/04/1_ARatpzvoAt1MVLfFej4Mg.png)

### 1. Stochastic Gradient Descent (SGD)
Takes a randomly selected subset of dataset for each training iteration
- randomness helps explore loss landscape

### 2. Momentum
Adjust update amount dynamically
- increase learning rate when slope is steep to prevent oscillations
- uses past gradients to guide overall direction

### 3. Modify Learning Rate (based on gradient)
Adapt learning rate to each parameter by making use of past gradients
- Ex optimizer algos: Adagrad, Adadelta, RMS Prop

### 4. Modify Learning Rate (based on training progress)
Learning rate is set based on training epoch and is independent of model's parameters at that point
## Overfitting
Main causes
- bias in training data
- not enough data
- bad local optimum

Adding additional losses can "break ties" between features to determine which is more impactful
- L2 does this with recognizing small weights

## Summary
Model learning learning "well" depends on complex interaction b/w
- initialization
- hypothesis class
- optimization
- data
- learning rate