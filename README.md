A Tutorial of Developing Android App for FLAX
=====================
This is a tutorial of how to build an Android App for FLAX learning platform using the FLAX Android Development Library. I will explain how to build a functional mobile App form a template project step by step.

Outline
----------
[TOC]


&nbsp;
&nbsp;
&nbsp;
Setting Up Environment
----------------------
Before we start programming, we need to set up the development environment, such as the IDE, the development SDK and the version control server.

### Download the ADT Bundle
In order to set up an Android development environment, developers need to download the ADT Bundle which includes the essential Android SDK components and a version of the Eclipse IDE with built-in ADT (Android Developer Tools). The download URL and more information can be seen here: http://developer.android.com/sdk/index.html

###	Import the FLAX Android application template
The template and library can be downloaded from the SVN server of the FLAX project `http://svn.greenstone.org/flax/trunk/flaxmain/mobile-resources/NewFlax/`. Open the ADT Bundle and import the projects `NewFlaxAndroidTemplate` and `NewFlaxAndroidLibrary` into the workspace. Then we have a simple template project that can be executed on a simulator or Android devices. When executing the template, it shows the primary user interfaces (Figure 1) of the application with blank pages remaining to be implemented.
<img src="http://i1150.photobucket.com/albums/o613/ouchxp/Primaryuserinterfaces.jpg" alt="Primary user interfaces" width="500px"/><br>
Figure 1: Primary user interfaces

Setting Up Project
----------------------
Now we have the template project which can be executed only based on the sample data. We need to set it up and make it become a project for the specific activity of FLAX we have in mind. 

### Setting Up the Source Code Structure
We start building the App with this template project. The first step is rename the project, packages and classes. Packages and classes should have univocal names that representing their role in the application. So we rename these names according to the naming rules.

**Example**
We use the Hangman activity as an example, use the FLAX activity name “hangman” as an identifier to turn the template project into a project for the Hangman activity. The comparison of before and after renaming is shown in the table below. 

|&nbsp;                         | Before (Template)        | After (Hangman)        |
|-------------------------------| -------------------------| -----------------------|
|*Project Name*                 | `NewFlaxAndroidTemplate` | `NewFlaxAndroidHangman`|
|*Package Name*                 | `flax.template.*`        | `flax.hangman.*`       |
|*Exercise Detail Entity Name*  | `TemplateExerciseDetail` | `HangmanExerciseDetail`|
|*Page Entity Name*             | `TemplatePage`           | `HangmanPage`          |
Table 1: Changed source structure


###	Setting Up Configurations
#### Configurate Strings
Open file `res/values/strings.xml` these are four names need to be change:

1.	The string tag named `app_name` represents the identifier of the application.
2.	The string tag named `app_name_launcher` represents the name of the launcher icon.
3.	The string tag named `name_on_home_screen` represents the large font name on the first screen `HomeScreen` when a user opens the application.
4.	The string tag named `list_screen_title` represents the title of `ListScreen`

**Example**
Before (Template)
```
    <string name="app_name">FlaxAndroidTemplate</string>
    <string name="app_name_launcher">Template</string>
    <string name="name_on_home_screen">TEMPLATE</string>
    <string name="list_screen_title">Template</string>
```
After (Hangman as an example)
```
    <string name="app_name">FlaxAndroidHangman</string>
    <string name="app_name_launcher">Hangman</string>
    <string name="name_on_home_screen">HANGMAN</string>
    <string name="list_screen_title">Hangman</string>
```
#### Configurate AppConfig
The application needs the URL of the XML containing the exercise list to retrieve data. This URL is defined in the file `assets/appConfig.properties`. Open this file and replace the value of item exerciseListUrl. 
```
#Before (Template)
exerciseListUrl.0 = ?a=pr&o=xml&ro=1&rt=r&s=TemplateActivityType&c=TemplateCollection&s1.service=11

#After (Hangman as an example)
exerciseListUrl.0 = ?a=pr&o=xml&ro=1&rt=r&s=Hangman&c=password&s1.service=11
```
> **NOTE:** One application can have multiple URLs in `appConfig`, simply increase the number at the end of `exerciseListUrl.n` in sequence (e.g. `exerciseListUrl.1 = ...`).



Implementing Entities
---------------------
After setting up the project, we need to implement entity classes. Entity classes are data models that join `Simple XML` and `OrmLite` together. Hence, we should consider both the XML file structure and the database schema when implementing entities. 

`ExerciseDetail` entity holding the data of exercise hint, score and status. `HangmanPage` entity contains the data for each page. The relation of these entities and base entities is shown in Figure 2.

<img src="http://i1150.photobucket.com/albums/o613/ouchxp/EntityUML.png" alt="Primary user interfaces" width="400px"/><br>
Figure 2: The relation between entity classes

We can implementing entities in four steps:

### Implement Entity POJOs
First, we start implementing entities as POJOs with empty constructor. `XXXXExerciseDetail` class extends `BaseExerciseDetail` and `XXXXPage` class extends `BasePage`. Then add data fields we need. 

- Add String or int type fields for normal fields. 
- Add a Collection type field with `XXXXPage` as its generic type into `XXXXExerciseDetail` and add a `XXXXExerciseDetail` type field into `XXXXPage` to represent the one-to-many relationship between exercise and page.

> **NOTE:** Several rules should follow when implementing entities.
>
> - Entity classes **MUST** have a public non-argument constructor. Otherwise, OrmLite and Simple 
XML are not able to create objects.
> - Entity classes of FLAX project **MUST** be subclass of `BaseEntity`, especially exercise detail class should extends `BaseExerciseDetail` class, page item class should extends `BasePage` class.
> - **MUST** use `Collection<?>` type to represent a set of node (or one-to-many relation). Because OrmLite only support this type of collection.

**Example**
*POJOs example (Hangman)*
```
    public class HangmanExerciseDetail extends BaseExerciseDetail {
    	private String hints;
    	private String from;
        // One-to-Many ("One" side keeping the "Many")
    	private Collection<HangmanPage> pages; 
    
    	/** Constructor */
    	public HangmanExerciseDetail() {}
    
    	/** Get/Set Methods */
    	...
    }

    public class HangmanPage extends BasePage{
    	private String word;
    	// One-to-Many ("Many" side keeping the "One")
    	private HangmanExerciseDetail exerciseDetail;
    	private String pressedKeys;
    	private int moves;
    	
    	/** Constructor */
    	public HangmanPage() {}
    
    	/** Get/Set Methods */
    	...
    }
```

### Add Annotations for Simple XML
After the POJOs were implemented, we should analysing the data structure of XML containing the exercise and add annotations for each field. 

- Use `@Root` to annotate `XXXXExerciseDetail` class, representing the root node of XML document.
- Use `@Attribute` to annotate fields corresponding to the `attribute="..."` attribute value of the node.
- Use `@Text` to annotate fields corresponding to the inner text of the child node.
- Use `@Element` to annotate fields corresponding to the child node.
- Use `@ElementList` to annotate fields corresponding to a set of node.
- Use `@Path` to change the current node path.

> **NOTE:** Detailed information of these annotations refer to this link http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php

**Example**
Use Hangman activity as an example, the root node of the XML document is `<response>`, then annotate `HangmanExerciseDetail` class with annotation `@Root(name = "response")`. The `from` field corresponding to the `from = "..."` attribute value of the `<response>` node, then annotate the `from` field with `@Attribute`. The collection of `HangmanPage` entity corresponding to the `<word>` node (One word per page in Hangman activity). So, annotate this collection field `pages` with `@ElementList(inline = true, entry = "word")` and annotate the `word` field in `HangmanPage` class with `@Text` to match the content of `<word>` node. 

*XML Document example contains exercise detail (Hangman)*
```
    <response from="default">
      <player hints="false">
        <word>accept</word>
        <word>maintain</word>
        <word>example</word>
      </player>
    </response>
```
*POJOs eanmple annotated for Simple XML (Hangman)*
```
    @Root(name = "response")
    public class HangmanExerciseDetail extends BaseExerciseDetail {
    
    	@Attribute
    	@Path("player")
    	private String hints;
    
    	@Attribute(name = "from")
    	private String from;
    
    	@Path("player")
    	@ElementList(inline = true, entry = "word")
    	private Collection<HangmanPage> pages;
    	...
    }
    
    public class HangmanPage extends BasePage{
	
    	@Text
    	private String word;
    	
    	private HangmanExerciseDetail exerciseDetail;
    	private String pressedKeys;
    	private int moves;
    	...
    }
```

**Advanced Concept**
Reduce nested object layers using XPath(`@Path`) & JavaBean (annotate get/set method)
http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#xpath
http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#javabean



### Add Annotations for OrmLite
We need to add annotations to entities for OrmLite to define the database schema. 

- Use `@DatabaseTable` to annotate the entities which corresponding to the database tables (e.g. , `XXXXExerciseDetail` class and `XXXXPage` class).

- Use `@DatabaseField` to annotate fields corresponding to the column in database table.
1. **MUST** assign argument `foreign` with boolean value `true` to indicate this field representing a foreign key.
2. **MUST** assign arguments `eager` with boolean value `true` and `maxEagerLevel` with int value `GlobalConstatn.MAX_EAGER_LEVEL`. In order to avoid multiple database queries and make sure call collection's get method multiple times will return the same object (make sure reference passing working correctly). Otherwise, lazy(default) foreign collection could be problematic when using Android adapters.

- Use `@ForeignCollectionField` to annotate collection fields that represent the one-to-many foreign relation.
1. **MUST** assign argument `foreignAutoRefresh`  with boolean value `true` to enable auto query. In order to avoid null values. 
2. `columnName` argument is optional, but recommend to give a meaningful name as the column name storing foreign keys.

> **NOTE:** Detailed information of these annotations refer to this link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html

**Example**
Use Hangman activity as an example. Annotate `HangmanExerciseDetail` class with `@DatabaseTable(tableName = "hangman_exercise")` to set a table name. Add `@DatabaseField` annotation for the fields that should be stored into database and annotate page collection field `pages` with `@ForeignCollectionField(eager = true, maxEagerLevel = MAX_EAGER_LEVEL)`. In order to make a one-to-many relation in the database. Also, the `exerciseDetail` field in `HangmanPage` class (for storing foreign key) should be annotated with `@DatabaseField(foreign = true, foreignAutoRefresh = true,` `columnName="exercise_foreign_id")`. Then add annotations for the rest of fields in `HangmanPage` class. 

 
```
    @DatabaseTable(tableName = "hangman_exercise")
    @Root(name = "response")
    public class HangmanExerciseDetail extends BaseExerciseDetail {
    
    	@DatabaseField
    	@Attribute
    	@Path("player")
    	private String hints;
    
    	@DatabaseField
    	@Attribute(name = "from")
    	private String from;
    
    	@ForeignCollectionField(eager = true, maxEagerLevel = MAX_EAGER_LEVEL)
    	@Path("player")
    	@ElementList(inline = true, entry = "word")
    	private Collection<HangmanPage> pages;
    	...
    }

    @DatabaseTable(tableName="hangman_page")
    public class HangmanPage extends BasePage{
    	
    	@Text
    	@DatabaseField
    	private String word;
    	
    	@DatabaseField(foreign = true, foreignAutoRefresh = true, 
    	        columnName="exercise_foreign_id") 
    	private HangmanExerciseDetail exerciseDetail;
    	
    	@DatabaseField(defaultValue= "")
    	private String pressedKeys;
    	
    	@DatabaseField(defaultValue= "0")
    	private int moves;
    	...
    }
```

### Build Foreign Relation for OrmLite
Use `@Commit` annotate a method, then this method will be called when the parsing process of this object was done. We can use this feature to build foreign relation or modifying data etc.

**Example**
Use Hangman activity as an example
```
    @DatabaseTable(tableName = "hangman_exercise")
    @Root(name = "response")
    public class HangmanExerciseDetail extends BaseExerciseDetail {
    	...
    	/**
    	 * Build one-to-many relation, prepare for orm.
    	 */
    	@Commit
    	private void buildRelation() {
    		for (HangmanPage page : pages) {
    			page.setExerciseDetail(this);
    		}
    	}
    	...
    }
```
<!-- Use HTML tag seperate URL into multi-lines in order to avoid format issue when converting to PDF -->
> **NOTE:** Detailed information of callback annotations refer to this link 
<a href="http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#callback">http&#58;//simple.sourceforge.net/download/stream/doc/tutorial</a>
<a href="http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#callback">/tutorial.php#callback</a>

Implementing Views
-----
### Setting Up Exercise Type
Before actually writing code for views, we need to define the exercise type of this FLAX activity. Create a constant in `LocalConstatnt` class by calling `ExerciseType.newInstance(...)` method. The three arguments of this method are: the class of exercise detail, the class of page, and the class of page fragment. 

**Example**
```
	public static final ExerciseType HANGMAN = ExerciseType.newInstance(
			HangmanExerciseDetail.class, HangmanPage.class, 
			GamePageFragment.class);
```
### Create Views
#### Create HomeScreen & ListScreen
HomeScreen and ListScreen are relatively easy to implement. Simply change the return value of getExerciseType method, return the type constant we created before. 

**Example**
```
public class HomeScreen extends BaseHomeScreenActivity{
	public ExerciseType getExerciseType() {
		return HANGMAN;
	}
	
	public Class<?> getNextActivityClass() {
		return ListScreen.class;
	}

	public int getExerciseName() {
		return R.string.name_on_home_screen;
	}
}

public class ListScreen extends BaseListScreenActivity {
	public ExerciseType getExerciseType() {
		return HANGMAN;
	}

	public Class<?> getNextActivityClass() {
		return GameScreen.class;
	}
}
```
#### Create GameScreen
In GameScreen we need to implement some basic logic such as calculate the score and possible score and setting up the page adapter. There are six methods have to be implemented in GameScreen class:

1.	getHowToPlayMessage method
This method should return a string value as a message of how this game to be played. (e.g. *Put `R.string.how_to_play_message` in the return statement as a return value for the Hangman activity.*)

2.	getHelpMessage method
This method should return a string value as a help message for user interface operations. (e.g. *Put `R.string.default_game_screen_help_message` in the return statement as a return value for the Hangman actitivy.*)

3.	getExerciseType method
This method is same as the `getExerciseType` method in `HomeScreen` and `ListScreen`, simply use the FLAX exercise type we defined in the `LocalConstans` class as a return value.

4.	calculatePossibleScore method
In this method, we should calculate the possible full score of the exercise. (e.g. *For the Hangman activity, one page worth one score. So, we simply return the max page number `mExerciseDetail.getPages().size()` as the full score.*)

5.	calculateScore method
In this method we should calculate the score of the current exercise status. (e.g. *For the Hangman activity, one page worth one score. Hence, we calculate the score by checking all the page status, summing the number of winning pages as score.*)

6.	getPageItemList method
This method should retrieve and return the page list from the exercise detail object. (e.g. *For Hangman activity, simply return `mExerciseDetail.getPages()`.*)

> **NOTE:** 
>
> - `GameScreen` class **MUST** extends `BaseGameScreenActivity<?, ?>` class
> - The two generic type of `BaseGameScreenActivity<?, ?>` **MUST** be class of `XXXXExerciseDetail` and `XXXXPage`
> - If need set a `ViewPager.OnPageChangeListener`, **MUST** set it on the `mPageIndicator` (not `mPagerAdapter`), because `mPageIndicator` need handles these event for drawing indicators. Otherwise, page indicator will not working correctly.
> - Operations related to the exercise (`ExerciseDetail` or `Exercise` class) should be written in `GameScreen`, and the operations for one page should implemented in `GamePageFragment`. 

&nbsp;
> **TIPS:**
> 
> - `mExercise` object is the item of exercise list in the `ListScreen`, it handles the `New`, `Incomplete` and `Complete` status.
> - `mExerciseDetail` object is the detail data of this exercise contains all pages, it handles the summary  and score of exercise.
> - Calling `getCurrentFragment()` can get the `Fragment` object of current page.
> - Calling getExerciseDao(), getPageDao(), getExerciseDetailDao(), can get DAOs for each entity type (table).
> - Data will be automatically saved when `onStop()` method be called. (When current `GameScreen` is not visible)


**Advanced Concept**
Here is a way of remove menu item form default menu. Override the `onCreateOptionsMenu` method and we can add or remove meun item form the default menu.
```
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean dispalyMenu = super.onCreateOptionsMenu(menu);

		// Hangman exercise doesn't need checkAnswer menu.
		MenuItem checkAnswerMenu = menu.findItem(R.id.check_answer);
		checkAnswerMenu.setVisible(false);

		return dispalyMenu;
	}
```


#### Create GamePageFragment

##### Implement layout file
The first step of creating a `GamePageFragment` is implementing the user interface of the game page. The layout file of game page is `res/layout/fragment_game_page.xml`. (e.g. *For the Hangman activity, we add an ImageView for showing the image of hanging man, a TextView for displaying the word to be guessed and 26 buttons for all the English letters.*)

##### Implement gaming logic
The main gaming logic should be implemented in `GamePageFragment` class. There two methods have to be implement: One is `checkAnswer` method. This method should check the status of current page, set status value to `PAGE_WIN` or `PAGE_FAIL` and save the page status. Another is `onCreateView` method, which should inflate the View object for every page and initiate components belong to each page.

> **NOTE:**
> 
> - `GamePageFragment` **MUST** extends `BasePageFragment<?>` class, the generic type of `BasePageFragment<?> ` **MUST** be class of `XXXXPage`.
> - **MUST** call the `mListener.onPageAnswerChecked` method at the end of the `checkAnswer` method. In order to trigger the score calculation process in `GameScreen`. 
> - When calling `GameScreen`'s method is required, we can use a listener like `OnPageAnswerCheckedListener`. Let `GameScreen` implement the interface, and bind the listener in `GamePageFragment`'s `onAttach` method. Call the method when necessary.

Then, we are free to write the gaming logic of our specific game.
> **TIPS:**
>
> - `mItem` object is the data of current page.
> - Call `getItemDao()` method can get the DAO of page entity (table).
> - Data can be save to database by calling `saveItem(mItem)` method.
> - Data will be automatically saved when `onStop()` method be called. (When current page is not visible)


