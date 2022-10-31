# Instruction

Please create `nlpserver.properties` under `<ProjectRoot>/speechcore/src/main/resources` before building first.

**nlpserver.properties**
```
nlp.host=<your nlp server ip>
nlp.port=9000
```

# Standford CoreNLP Server
Stanford CoreNLP server is needed to parse the text provided by the speech recognizer. You may download it here:

[Download - CoreNLP](https://stanfordnlp.github.io)

Simply extract and run this command from where it was extracted to:
```
#/bin/sh

java -mx3g -cp stanford-corenlp-4.5.1.jar:stanford-corenlp-4.5.1-models.jar:joda-time.jar:jollyday.jar:protobuf-java-3.19.2.jar:xom.jar:ejml-core-0.39.jar:ejml-simple-0.39.jar:ejml-ddense-0.39.jar:jaxb-api-2.4.0-b180830.0359.jar:jaxb-impl-2.4.0-b180830.0438.jar edu.stanford.nlp.pipeline.StanfordCoreNLPServer -preload tokenize,pos,parse,sentiment,ssplit
```
