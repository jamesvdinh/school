## FNN
> Feedforward Neural Networks: simplest type of neural network consisting of a series of layers that are connected in a forward direction

Issues with this method:
- ex. 2 images that are the same, one is translated to the right slightly.
	- the information is the same, but the feature vectors are different

## Convolution
> MLP (multilayer perceptron): name for a modern FNN consisting of fully connected neurons w/ nonlinear activation functions

Captures local patterns of data

1. Take a 1D image
2. Filter / theta / weight / parameter (all the same term)
	- random -> this is our theta
	- filter map
3. after-convolution
	- dot product of initial vector w/ filter
4. After ReLu

Ex.
0, 0, **1, 1, 1**, 0, 1, 0, 0, 0
    **-1, 1, -1**
-1, 0, **-1**, 0, -2, 1, -1, 0
0, 0, 0, 0, 0, 1, 0, 0

Output is smaller
Remedy: pad with zeros

Point is to transform the initial input vector and create a matrix that is diagonal
- reduces the # of parameters
- theta does not grow w/ input
- the neuron's input layers are shared

### Examples of Convolutions
Sharpening, Blurring, Edge detection

Image * mask (kernel / filter) = Output

Multiple channels/filters
- input (x) -> R
	- dim 3 * H * W
- Channels
	- dim 3 * k * k *  C-out

## Pooling
Spatial invariant method to summarize features
We wish to know if a feature is there
- essentially another kernel

Max pooling
- similar to filtering, but output the maximum entry instead of a weighted sum
- point is to reduce the input layer for next layer (CNN block)

## Summary
Network is still trained via backpropogation