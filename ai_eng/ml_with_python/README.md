

## Machine learning paradigm

 - Supervised Learning ( models train on labeled data)
    - Classification (predicts class or catagory of a case)
    - Regression (predicts continuous values)
 - Unsupervised Learning ( models train on unlabeled data)
    - Clustering (groups similar data)
    - Association (finds relationships between variables)
    - Anamoly Detection (identifies unusual patterns)
    - Sequence Prediction (predicts next value in a sequence)
    - Dimensionality Reduction (reduces number of variables)
    - Recommendation Systems (predicts user preferences)
 - Semi-supervised Learning ( models train on a mix of labeled and unlabeled data)
 - Reinforcement Learning ( models learn by interacting with an environment)





### Machine Learning Techniques

 - Classification (predicts class or catagory of a case)
 - Regression (predicts continuous values)
 - Clustering (groups similar data)
 - Association (finds relationships between variables)
 - Anamoly Detection (identifies unusual patterns)
 - Sequence Prediction (predicts next value in a sequence)
 - Dimensionality Reduction (reduces number of variables)
 - Recommendation Systems (predicts user preferences)



 ### Classification Vs Regression

 - Classification is used when the output is categorical
 - Regression is used when the output is continuous


 ### Clustering Vs Association

 - Clustering is used when the output is categorical and the data is unlabeled
 - Association is used when the output is categorical and the data is labeled


 ## Machine Learning Model Lifecycle
  - Problem Definition
  - Data Collection
  - Data Preprocessing
  - Model Training
  - Model Evaluation
  - Model Deployment
  - Model Monitoring
  - Model Maintenance


  ### Data Scientist
   - Data Storyteller
   - Use Descriptive, predictive  analytics 
   - eg for Descriptive : EDA, Clustering 
   - eg for predictive : Regression, Classification
   - DATA : Need structured or tabular data mostly. (post cleaning)
   - MODELS : Data science models (Statistical models, ML models)
      - Scope of individual model is limited or narrow.
      - Smaller in size in terms of number of parameters.
      -  Requires less compute power and memory.
      -  Less time to train. (Hours to Days)
   - PROCESS : use case -> data -> model(train & validate using feature engineering, cross validation, hyperparameter tuning) -> deploy -> monitor -> maintain
      

  

  

### AI Engineer
  - AI System Builder
  - prescriptive AND Generative AI
  - eg for prescriptive : Decision optimization, Recommendation Systems
  - eg for Generative AI : Intelligent assistants, Content generation, Chatbots.
  -  DATA : Mainly unstructured data (text, images, audio, video)
  - MODELS : Foundational Models (LLMs, VLMs, etc.)
     - Scope of individual model is broad.
     - Larger in size in terms of number of parameters.(Billions to Trillions)
     - Requires more compute power and memory.(Requires clusters of GPUs/TPUs)
     - More time to train. (Weeks to Months)
   - PROCESS : use case -> working with a pretrained model -> prompt engineering(chaining, PEFT, RAG, Agents), fine-tuning -> embeding -> deploy -> monitor -> maintain  