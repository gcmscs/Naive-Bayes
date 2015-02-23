Naive Bayes Algo Implementation 

I used use a benchmark dataset that is frequently used in text categorization problems.
This dataset, Reuters-21578, consists of documents that were appeared in Reuters newswire in 1987. Each document was then manually categorized into a topic 
among over 100 topics. We are given documents on only “earn” and “acquisition” (acq) topics, so we will be using a shortened version of the dataset 
(documents assigned to topics other than "earn" or "acq" are not in the dataset). As features, we use the frequency (counts) of each word occurred in the document.
This model is known as bag of words model and it is frequently used in text categorization.


We have following files:
train.csv: Training data. Each row represents a document, each column separated by commas represents
features (word counts). There are 4527 documents and 5180 words.
train-labels.txt: labels for the training data
test.csv: Test data, 1806 documents and 5180 words
test-labels.txt: labels for the test data
word-indices: words corresponding to the feature indices
