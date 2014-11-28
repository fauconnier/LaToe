# LaToe


[LaToe](https://github.com/jfaucon/latoe) (*Layout Annotation for Text Object Extraction*) is a tool that extracts the layout of various formats (HTML, MediaWiki, PDF) to identify particular text objects (such as hierarchical structures).




## Install

* Clone LaToe Project:
```bash
 git clone https://github.com/jfaucon/latoe
 cd latoe
```

* Build LaToe with Ant:
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
java -jar latoe-0.x.x.jar format=[*wiki-text|wiki-bdd|html|pdf] file=[path or name in bdd] 
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

* For MediaWiki in BDD:
```bash
 java -jar LaToe_0.0.1.jar format=wiki-bdd file=name_of_page
```

* For PDF:
```bash
 java -jar LaToe_0.0.1.jar format=pdf file=./samples/sample.pdf
```





## Projects used:
* For PDF:
    * [LaPDFText](http://code.google.com/p/lapdftext/)
    * [corpus-LING_GEOP](https://github.com/jfaucon/corpus-LING_GEOP) to train the model
    
* For MediaWiki:
    * [JWPL](https://code.google.com/p/jwpl/)
    * Wikipedia Dump 20140928 (FR)
    
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

    







