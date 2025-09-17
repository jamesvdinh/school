# Machine Learning Foundations I

LDCT: Low Dose CT scan
NLST: National Lung Screening Trial

ppl who have lung cancer:
- 74% not NLST eligible
- 26% NLST eligible

> Problem: cost-effectiveness issue

## Loss functions

### Log linear loss

multiplcation of likelihoods -> converges to 0 very quickly

### Cross Entropy loss

addition of log likelihoods -> stable precision

## Model function optimization

how to find model function h?

> Answer : Gradient Descent

## Learning Rate

LR too small -> slow convergence
LR too large -> diverges

we want training curve to have gradual decay

- graph should decrease slowly

## Summary

we want h to do well on NEW patients
want better ROC curve to be useful clinically

# Thurs

Doctors code diagnoses to get paid -> not necessarily reflective of what the patient has or might have

- not "cleaned" for use in a model, for example
- Medial Coders / AI scribes scour through medical charts to code as many procedures / diagnoses as possible -> maximize payment from insurance companies


## Abridge

- Good for picking up "dry" clinical observations
- Misses a lot of "human" component part of health
  - i.e. consumes an almond joy bar after work

## Take home points

- treat whole patient, not individual diseases
- most of healthcare is delivered outside health care system
- partner w/ physicians to understand real Problem
