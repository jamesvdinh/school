## Maximum Likelihood Estimate
Properties
- consistency: as we get more data (drawn from one distribution in our family), then we **converge** to estimating the true value of $\theta$ for $D$ 
- Statistically efficient: making good use of the data ("*least variance*" parameter estimates)
- The value of $p(D|\theta_{MLE})$ is *invariant* to re-parameterization
	- e.g. $N(x|\mu, \sigma)$ -> $N(x|2 + \mu, \sigma)$
- MLE can still yield a parameter estimate even when the data was not generated from that family (phew & caveat emptor)

### MLE for Univariate Gaussian
Ends up being the mean of the data, or $\mu$

### MLE yields a "point estimate" of our parameter
- when performing MLE, we get just one single estimate of the parameter, $\theta$, rather than a distribution over it (which captures uncertainty)
- in Bayesian statistics, we obtain a (posterior) distribution over $\theta$, that is $p(\theta|D)$, instead of $\theta_{MLE}$

> $D$ = distribution
> $\theta$ = parameter

### e.g. MLE for multinomial distribution
Consider a six-sided die that we will roll: we want to know the probability of each side of the die turning up ($\theta = \theta_1 ... \theta_6$)
- assume we have observed N rolls, with RV $X ~ p_\theta(X)$
- we write that $P(X = k|\theta) = \theta_k$

![[Pasted image 20260203153137.png]]
