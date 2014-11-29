# LaToe


[LaToe](https://github.com/jfaucon/latoe) (*Layout Annotation for Textual Object Extraction*) is a tool which extracts text layout from HTML, MediaWiki, or PDF documents for identifying specific textual objects (such as enumerative structures).




## Install

* Clone LaToe Project:
```bash
 git clone https://github.com/jfaucon/latoe
 cd latoe
```

* Build LaToe with Ant (or use the .jar in ./dist/ directory for Java 1.7):

```bash
 ant dist
```

* Run a test:
```bash
 cd dist
 java -jar LaToe_0.0.1.jar format=wiki-text file=./samples/sample.mediawiki 
```



## Usage

* General usage:
```bash
Usage:
java -jar LaToe-0.x.x.jar command=[*analyse|deploy-bdd|train-pdf]

if command=analyse
format=[*wiki-text|wiki-bdd|html|pdf] file=[path or name in bdd]

if command=deploy-bdd
dir=[path to dir] lang=[*fr|en]

if command=blockify-pdf
dir=[path to directory of pdf]

if command=train-pdf
train=[path to training set] outputModel=[path for new model]

	* indicates default values for args.

```


* For HTML:
```bash
 java -jar LaToe_0.0.1.jar format=html file=./samples/sample.html
```

* For MediaWiki text:
```bash
 java -jar LaToe_0.0.1.jar format=wiki-text file=./samples/sample.mediawiki
```

* For MediaWiki in a database:
```bash
 java -jar LaToe_0.0.1.jar format=wiki-bdd file=name_of_page
```

* For PDF:
```bash
 java -jar LaToe_0.0.1.jar format=pdf file=./samples/sample.pdf
```

## Deploy a Wikipedia Dump

LaToe uses [JWPL](https://code.google.com/p/jwpl/) to deal with a large Wikipedia database. A documentation for deploy English dump can be [found](https://code.google.com/p/jwpl/wiki/DataMachine) on the website of JWPL. For the french and with LaToe, you can follow the procedure (here for the french):

* Download a Wikipedia Dump [here](https://dumps.wikimedia.org/frwiki/). You need 3 specific files:
    * frwiki-xxxxx-categorylinks.sql.gz
    * frwiki-xxxxx-pagelinks.sql.gz
    * frwiki-xxxxx-pages-articles.xml.bz2

* Run transformation in text files. Warning: you will need at least 2GB of memory and the processing could take some time. At the end of this step, you can find 11 text files in the directory of dump. To begin the transformation:
```bash
 java -Xmx2g -jar LaToe_0.0.1.jar command=deploy-bdd dir=/path/to/dump/ lang=fr
```
    
* Create the database in MySQL:
```bash
  mysqladmin -u[USER] -p create [DB_NAME] DEFAULT CHARACTER SET utf8;
```

* Import the 11 text files from the directory of dump:
```bash
  mysqlimport -uUSER -p --local --default-character-set=utf8 {database_name} `pwd`/*.txt 
```

* For the first connection, the indexing of database could take some time (~ 30 min)


## Adding Wikipedia Templates
  
In Wikipedia texts, you can find a large number of "Templates". A Template is a model used in MediaWiki to add an information in the text. You can find more information [here](http://fr.wikipedia.org/wiki/Aide:Mod%C3%A8le).

For instance, "{{-s|XX|e}}" will be translated in text as "XXe siècle". LaToe allows to deal with some common templates.

In directory "./configuration/MediaWikiParsing/", you will find 4 texts files:

* category_identifiers.txt
* image_identifiers.txt
* delete_templates.txt
* parser_templates.txt

The file "delete_templates.txt" contains the list of all templates which will be deleted during the parsing of a MediaWiki text. You could use Java Regex to improve the recall of those models (e.g: ".*"). For instance:
```bash
  Voir_homonymes
  à_sourcer
  DEFAULTSORT.*
  ...
```


The file "parser_templates.txt" contains the list of all templates which will be parsed during the process. For instance:
```bash
  VIe_siècle[VIe siècle]
  unité[0 1]
  citation[" all "]
  ...
```

A simple system allows to replace those templates with particular values. For instance,

* {{VIe_siècle}} will be substituted by "VIe siècle"
* {{unité|10|km}} will be substituted by "10 km"
* {{citation|..some text..}} will be substituted by " " ..some text.." "


By default, if a template is not contained in "delete_templates.txt" and "parser_templates.txt", its content will be added in the text with no special formatting. To delete all templates, you can use the pattern ".*" in "delete_templates.txt".




## PDF processing

Currently, the CRF model used for the PDF is trained a small corpus ([LING_GEOP](https://github.com/jfaucon/corpus-LING_GEOP) - 46 documents).  
This implies that LaToe can be not efficient for an unseen PDF document with a layout varying from the PDF documents in LING_GEOP. Note that the CRF implementation is the Sarawagi's [CRF](http://crf.sourceforge.net/).

Two solutions can be used to improve the PDF processing of a new target corpus:

* Train a new model,
* Using rules.


### Train a new model

Two steps to train a new model: (i) annotation of a set of new documents, (ii) build the model. Note: the language of documents is relatively independent of the process. For instance: you can train on a English corpus and  treat a French corpus. 


* Blockify PDF files. Is done using [LaPDFText](http://code.google.com/p/lapdftext/).
```bash
  java -jar LaToe_0.0.1.jar command=blockify-pdf dir=/path/to/pdf
```

* For instance, you can try with the data in "./data/blockify_pdf/":
```bash
  java -jar LaToe_0.0.1.jar command=blockify-pdf dir=./data/blockify_pdf/
```

* The visual blocs of PDF files will be contained in XML files. For instance for "./data/blockify_pdf/sample2_spatial.xml":
```xml
        <Chunk x1="270" y1="64" x2="403" y2="74" type="unclassified">
            <Word x1="270" y1="64" x2="319" y2="74"
                font="AGaramond-Regular" style="font-size:9pt">POLITIQUE</Word>
            <Word x1="322" y1="64" x2="374" y2="74"
                font="AGaramond-Regular" style="font-size:9pt">ETRANGERE</Word>
            <Word x1="377" y1="64" x2="382" y2="74"
                font="AGaramond-Regular" style="font-size:9pt">2</Word>
            <Word x1="385" y1="64" x2="403" y2="74"
                font="AGaramond-Regular" style="font-size:9pt">2003</Word>
        </Chunk>
```

* You have to replace "unclassified" tag by a tag such as:
    * h1, h2, h3, etc.
    * documentTitle
    * item
    * p
    * quote
    * footer
    * header
    * byline
    * footnote
    * other
    
* You can modify the configuration of CRF model in "./configuration/PDFParsing/crf.conf". The description of [options](http://crf.sourceforge.net/introduction/parameters.html) can be found on the website of CRF:
```bash
    modelGraph=naive
    debugLvl=1
    maxIters=30
    debugMode=0
```


* Train the model.
```bash
  java -jar LaToe_0.0.1.jar command=train-pdf dir=/path/to/xml outputModel=/path/to/newmodel
```
* Note that you can use the corpus [LING_GEOP](https://github.com/jfaucon/corpus-LING_GEOP) with your new annotated documents:
```bash
  java -jar LaToe_0.0.1.jar command=train-pdf dir=./data/training_pdf/LING_GEOP/ outputModel=./models/newmodel.bin
  
    Number of features :383
    Number of training records 46
    Iter 0 loglikelihood -16189.166823368809 gnorm 5031.276561629522 xnorm 0.0
    Iter 1 loglikelihood -11744.692443950818 gnorm 3750.8122671101482 xnorm 1.0000000000000004
    Iter 2 loglikelihood -9127.37648798875 gnorm 3616.2844388786966 xnorm 4.839337410745183
    Iter 3 loglikelihood -7659.547444164803 gnorm 4757.633374803437 xnorm 4.812996731727109
    ...
    Iter 30 loglikelihood -1657.6455758012862 gnorm 151.30609234675495 xnorm 24.680474008932563
    Serialize model to ./models/newmodel.bin

```


* Test the model:
```bash
  java -jar LaToe_0.0.1.jar command=analyse format=pdf file=./samples/sample.pdf inputModel=/path/to/newmodel
```



### Using rules (TODO)

It would be necessary to adapt [LaPDFText](http://code.google.com/p/lapdftext/) to Java 1.7 (or higher). To do that, it is necessary to change the Drools library (see [here](https://issues.jboss.org/browse/JBRULES-3163) for bug report for Drools).



## See results

You can use two annotation tools to treat/see results: LARAt and Glozz. LaToe allows also an output in .svg describing the logical tree of document.s

### LARAt

[Larat](https://github.com/jfaucon/LARAt) is simple annotation tool dedicated for enumerative structures. It takes in input a HTML file and gives an output in XML. To run LARAt, use:
```bash
  java -jar LaToe_0.0.1.jar command=startlarat
```

![Alt text](./resources/pics/front_end.png?raw=true "LARAt interface") 



### GLOZZ

[Glozz](http://www.glozz.org) is a powerful annotation tool which can deal with various textual objects. Glozz is not included in LaToe, but you can download [here](http://www.glozz.org).

LaToe gives in output two files:

* .ac file : file which contains the text of file.
* .aa file : file which contains typographic annotations and the annotations relative to textual objects.




### TreeLayout

[TreeLayout](http://code.google.com/p/treelayout/) is a java library dedicated to print Tree. LaToe allows to print a simple tree-representation of document. To see more information about the internal model used, see below (Fauconnier, 2014) in french or contact me.

Excerpts for "Volcan" Wikipedia page:

* Begin of document:
![Alt text](./resources/pics/volcan1.png?raw=true "Volcan Tree") 

* Root of document:
![Alt text](./resources/pics/volcan2.png?raw=true "Volcan Tree") 



## Projects used in LaToe:
* For PDF:
    * [LaPDFText](http://code.google.com/p/lapdftext/)
    * [corpus-LING_GEOP](https://github.com/jfaucon/corpus-LING_GEOP) to train the model
    
* For MediaWiki:
    * [JWPL](https://code.google.com/p/jwpl/)
    
* For HTML:
    * [jsoup](http://jsoup.org/)
    * [htmlcleaner](http://htmlcleaner.sourceforge.net/)
    * [juniversalchardet](http://code.google.com/p/juniversalchardet/)
   
    
* Others projects:
    * as Natural Language Processing tools:
        * [ACABIT](http://www.bdaille.com/index.php?option=com_content&task=blogcategory&id=5&Itemid=5) and [ACABIT-Client](https://github.com/jfaucon/acabit-client)
        * [YaTeA](http://search.cpan.org/~thhamon/Lingua-YaTeA/) and [YaTeA-Client](https://github.com/jfaucon/yatea-client)
        * [Talismane](https://github.com/urieli/talismane) and [Talismane-Client](https://github.com/jfaucon/talismane-client)
        * [Frej](http://frej.sourceforge.net) and [FuzzyMatcher-Server](https://github.com/jfaucon/fuzzymatcher-server)
        
    * as Machine Learning tools:
        * MaxEnt implementation [OpenNLP](https://opennlp.apache.org/)
        * MaxEnt implementation [AMI](https://github.com/jfaucon/AMI)
        * SVM implementation [e1071](http://cran.r-project.org/web/packages/e1071/index.html) 
        * Sarawagi's [CRF](http://crf.sourceforge.net/)
        
    * as Import/Export tools:
        * [OpenCSV](http://opencsv.sourceforge.net/)
        * [TreeLayout](http://code.google.com/p/treelayout/)

    * as Annotation tools:
        * [Larat](https://github.com/jfaucon/LARAt)
        * [Glozz](http://www.glozz.org) (Not included)


## References

* Aubin, S. & Hamon, T. Improving term extraction with terminological resources Advances in Natural Language Processing, Springer, 2006, 380-387

* Daille, B. "Study and implementation of combined techniques for automatic extraction of terminology," The balancing act: Combining symbolic and statistical approaches to language, vol. 1, pp. 49-66, 1996.

* Fauconnier, J. -P.; Kamel, M. and Rothenburger, B. "Une typologie multi-dimensionnelle des structures énumératives pour l'identification des relations termino-ontologiques," in Proc. International Conference on Terminology and Artificial Intelligence (TIA), 2013.

* Fauconnier, J. -P.; Sorin, L.; Kamel, M.; Mojahid, M.; and Aussenac-Gilles, N. "Détection automatique de la structure organisationnelle de documents à partir de marqueurs visuels et lexicaux," in Proc. Actes de la 21e Conférence sur le Traitement Automatique des Langues Naturelles (TALN 2014), 2014, pp. 340-351.

* Ramakrishnan, C.; Patnia, A.; Hovy, E. H. and Burns, G. "Layout-aware text extraction from full-text PDF of scientific articles.," Source code for biology and medicine, vol. 7, iss. 1, 2012.

* Urieli, A.  "Robust French syntax analysis: reconciling statistical methods and linguistic knowledge in the Talismane toolkit," PhD Thesis , 2013.

* A. Widlöcher and Y. Mathet, "The Glozz platform: a corpus annotation and mining tool," in Proc. Proceedings of the 2012 ACM symposium on Document engineering, 2012, pp. 171-180.

* Zesch, T.; Müller, C. & Gurevych, I. "Extracting Lexical Semantic Knowledge from Wikipedia and Wiktionary", Proceedings of the 6th International Conference on Language Resources and Evaluation, 2008


    







