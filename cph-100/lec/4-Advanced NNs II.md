Initialization, BatchNorm only improve the training process, not completely fix it

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