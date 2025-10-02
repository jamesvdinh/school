## Attention Pooling: learned weighted avg
Replaces traditional pooling (like max or average) by using an attention mechanism to determine the most important features to keep and aggregate

a.k.a **Weighted Average**
- learnable & context-aware

Steps:
1. input: a feature map from preceding convolution layer
2. Attention weight calculation: an auxiliary network (often a simple CNN block, FFN) processes the feature map -> **attention map**
	- has same spatial dimensions as input map and contains scalar weight for every feature element
3. Feature Weighting: original feature map is multiplied by attention map
4. Aggregation (pooling): weighted features are then aggregated; typically a weighted sum over the pooling region, effectively integrating the  features based on calculated importance
