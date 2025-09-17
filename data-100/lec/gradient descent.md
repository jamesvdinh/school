# Gradient Descent

## Gradient Function

> $$\large \nabla_{\vec\theta} L (\vec\theta) = \dfrac{1}{n}\sum_{i=1}^{n}\nabla_{\vec\theta} l(y_i, \hat{y_i})$$

### Update parameters

> $$\large \vec\theta = \vec\theta - \alpha\nabla L(\vec\theta)$$

- **Epoch**: One pass through all n data points while updating the parameters

### asymptotics

- runtime is O(nd)
  - n: loss computations (epochs)
  - d: # parameters

### Batch GD (BGD)

> Computes gradient on **entire dataset**

- computes **true gradient**
- always descends towards true minimum of loss
- computes loss of **b** data points

### Mini-batch GD (MBGD)

> Computes gradient on a **small subset** of data points

- e.g. 32 or 128
- **approximates** true gradient
- reduces variance in updates
- may not descend towards true minimum w/ each update

### Stochastic Gradient Descent (SGD)

> Computes gradient on **a single randomly chosen point**

- uses randomly selected individual data points rather than entire dataset
- can perform _online learning_ (learn continuously as data arrives)
