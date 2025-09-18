## gradient descent
$$\theta^2_t = \theta^2_{t-1} - \mu \frac{\partial L}{\partial \theta^2}$$

calculating gradient

NN diagram:
1. inputs: a
2. Layers: $L1, L2$
3. Output: a

$$a = \theta Z^1 + b$$
$$\frac{\partial L}{\partial \theta^2} = \frac{\partial L}{\partial p} \frac{\partial p}{\partial Z^2} *\frac{\partial Z^2}{\partial a^2} \frac{\partial a^2}{\partial \theta^2}$$ [![Gradient Descent vs. Backpropagation: What's the Difference?](https://editor.analyticsvidhya.com/uploads/18870backprop2.png)

## Underfitting
Main causes
- bad initialization
- bad local minimum
- bad optimization