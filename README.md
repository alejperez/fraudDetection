# Detección de fraudes en transacciones comerciales mediante modelos de grafos.

Fraud Detection prototype based on graph database

Among the different methods for detecting fraud in banking transactions, one of the most employed ones is the analysis of relationships between the participants of the transactions using graph-based models. Querying these relationships can detect false profiles, irregular transactions, and other signs of possible fraud.

The methodology will be the implementation of a graph-based banking transaction fraud detection system, using Neo4j technology. It will analyse the information from bank transactions, establishing the possible relationships between the parties, locations, dates, so that common patterns of fraud can be detected, through rings of false identities, transactions out of context or similar.

Due to the difficulty of getting access to real financial data. Different sets of transaction data will be generated, and the graph-based models necessary to establish the appropriate queries to search for fraud patterns.

The outcome of the project, apart from documentation, will be the detection model used, the required queries, and the reports generated from those queries. The software will include the model for Neo4j and the necessary cypher code and, if possible, Java code to implement Neo4j triggers for immediate execution when a transaction occurs. Also, a small program to generate banking transactions will be developed.

With these results, it is expected to show a practical case of fraud detection using this technology, as well as future improvements combined with other techniques, such as rule-based models or classification methods.

========================================================================================


Detección de fraudes en transacciones comerciales mediante modelos en grafos.

Dentro de los distintos métodos de detección de fraudes en transacciones bancarias, uno de los empleados sería el análisis de relaciones entre los participantes de las transacciones mediante modelos basados en grafos. Consultando estas relaciones pueden detectarse perfiles falsos, transacciones irregulares, y otros indicios de posible fraude.

La metodología empleada será la implementación de un sistema de detección de fraude en transacciones bancarias basadas en grafos, empleando la tecnología de Neo4j. Analizará la información de estas transacciones, estableciendo las relaciones posibles entre los intervinientes, ubicaciones de éstos, fechas, de forma que se puedan detectar patrones habituales de fraude, a través de anillos de identidades falsas, transacciones fuera de contexto o similares.

Debido a la dificultad de disponer de datos financieros reales. Se  distintos conjuntos de datos de transacciones, y los modelos basados en grafos necesarios para establecer las consultas adecuadas para la búsqueda de patrones de fraude. 

Los artefactos generados, además de la documentación, serán el modelo empleado de detección, así como las consultas requeridas, y los informes generados a partir de esas consultas. El software incluirá el modelo para Neo4j y código cypher necesario y, si cabe, código Java para implementar triggers Neo4j para ejecución inmediata al producirse una transacción. Se desarrollará también un programa para generar transacciones bancarias de prueba

