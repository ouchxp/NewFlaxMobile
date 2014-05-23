NewFlaxMobile
=============

http://flax.nzdl.org

A Tutorial of Developing Android App for FLAX	{#welcome}
=====================
This is a tutorial of how to build an Android App for FLAX learning platform using the FLAX Android Development Library. I will explain how to build a functional mobile App form a template project step by step.

Outline
----------
[TOC]

Setting Up Environment
----------------------
Before we start programming, we need to set up the development environment, such as the IDE, the development SDK and the version control server.

### Download the ADT Bundle
In order to set up an Android development environment, developers need to download the ADT Bundle which includes the essential Android SDK components and a version of the Eclipse IDE with built-in ADT (Android Developer Tools). The download URL and more information can be seen here: http://developer.android.com/sdk/index.html

###	Import the FLAX Android application template
The template and library can be downloaded from the SVN server of the FLAX project http://svn.greenstone.org/flax/trunk/flaxmain/mobile-resources. Open the ADT Bundle and import the projects NewFlaxAndroidTemplate and NewFlaxAndroidLibrary into the workspace. Then we have a simple template project that can be executed on a simulator or Android devices. When executing the template, it shows the primary user interfaces (Figure 1) of the application with blank pages remaining to be implemented.
<center><img src="http://i1150.photobucket.com/albums/o613/ouchxp/Primaryuserinterfaces.jpg" alt="Primary user interfaces" width="500px"/>
Figure 1: Primary user interfaces</center>

Setting Up Project
----------------------
Now we have the template project which can be executed only based on the sample data. We need to set it up and make it become a project for the specific activity of FLAX we have in mind. 

### Setting Up the Source Code Structure
We start building the App with this template project. The first step is rename the project, packages and classes. Packages and classes should have univocal names that representing their role in the application. So we rename these names according to the naming rules.

We use the Hangman activity as an example, use the FLAX activity name “hangman” as an identifier to turn the template project into a project for the Hangman activity. The comparison of before and after renaming is shown in the table below. 
<center>
&nbsp;                         | Before (Template)        | After (Hangman)
---------                      | --------------------     | -------
*Project Name*                 | `NewFlaxAndroidTemplate` | `NewFlaxAndroidHangman`
*Package Name*                 | `flax.template.*`        | `flax.hangman.*`
*Exercise Detail Entity Name*  | `TemplateExerciseDetail` | `HangmanExerciseDetail`
*Page Entity Name*             | `TemplatePage`           | `HangmanPage`
Table 1: Changed source structure
</center>

###	Setting Up Configurations
#### Configurate Strings
Open file `res/values/strings.xml` these are four names need to be change:

1.	The string tag named `app_name` represents the identifier of the application.
2.	The string tag named `app_name_launcher` represents the name of the launcher icon.
3.	The string tag named `name_on_home_screen` represents the large font name on the first screen `HomeScreen` when a user opens the application.
4.	The string tag named `list_screen_title` represents the title of `ListScreen`

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






Welcome to StackEdit!	{#welcome}
=====================


Hello, I am your first Markdown document within **StackEdit**[^stackedit]. Don't delete me, I can be helpful. I can be recovered anyway in the `Utils` tab of the <i class="icon-cog"></i> `Settings` dialog.

----------


Documents
---------

**StackEdit** stores your documents in your browser, which means all your documents are automatically saved locally and are accessible **offline!**

> **NOTE:**
> 
> - StackEdit is accessible offline after the application has been loaded for the first time.
> - Your local documents are not shared between different browsers or computers.
> - Clearing your browser's data may **delete all your local documents!** Make sure your documents are backed up using **Google Drive** or **Dropbox** synchronization (see [<i class="icon-share"></i> Synchronization](#synchronization) section).

#### <i class="icon-file"></i> Create a document

You can create a new document by clicking the <i class="icon-file"></i> button in the navigation bar. It will switch from the current document to the new one.

#### <i class="icon-folder-open"></i> Switch to another document

You can list all your local documents and switch from one to another by clicking the <i class="icon-folder-open"></i> button in the navigation bar.

#### <i class="icon-pencil"></i> Rename a document

You can rename the current document by clicking the document title in the navigation bar.

#### <i class="icon-trash"></i> Delete a document

You can delete the current document by clicking the <i class="icon-trash"></i> button in the navigation bar.

#### <i class="icon-hdd"></i> Save a document

You can save the current document to a file using the <i class="icon-hdd"></i> `Save as...` sub-menu from the <i class="icon-provider-stackedit"></i> menu.

> **Tip:** See [<i class="icon-share"></i> Publish a document](#publish-a-document) section for a description of the different output formats.


----------


Synchronization
---------------

**StackEdit** can be combined with **Google Drive** and **Dropbox** to have your documents centralized in the *Cloud*. The synchronization mechanism will take care of uploading your modifications or downloading the latest version of your documents.

> **NOTE:**
> 
> - Full access to **Google Drive** or **Dropbox** is required to be able to import any document in StackEdit.
> - Imported documents are downloaded in your browser and are not transmitted to a server.
> - If you experience problems exporting documents to Google Drive, check and optionally disable browser extensions, such as Disconnect.

#### <i class="icon-download"></i> Import a document

You can import a document from the *Cloud* by going to the <i class="icon-provider-gdrive"></i> `Google Drive` or the <i class="icon-provider-dropbox"></i> `Dropbox` sub-menu and by clicking `Import from...`. Once imported, your document will be automatically synchronized with the **Google Drive** / **Dropbox** file.

#### <i class="icon-upload"></i> Export a document

You can export any document by going to the <i class="icon-provider-gdrive"></i> `Google Drive` or the <i class="icon-provider-dropbox"></i> `Dropbox` sub-menu and by clicking `Export to...`. Even if your document is already synchronized with **Google Drive** or **Dropbox**, you can export it to a another location. **StackEdit** can synchronize one document with multiple locations.

> **Tip:** Using **Google Drive**, you can create collaborative documents to work in real time with other users. Just check the box `Create a real time collaborative document` in the dialog options when exporting to Google Drive.

#### <i class="icon-refresh"></i> Synchronize a document

Once your document is linked to a **Google Drive** or a **Dropbox** file, **StackEdit** will periodically (every 3 minutes) synchronize it by downloading/uploading any modification. Any conflict will be detected, and a local copy of your document will be created as a backup if necessary.

If you just have modified your document and you want to force the synchronization, click the <i class="icon-refresh"></i> button in the navigation bar.

> **NOTE:** The <i class="icon-refresh"></i> button is disabled when you have no document to synchronize.

#### <i class="icon-refresh"></i> Manage document synchronization

Since one document can be synchronized with multiple locations, you can list and manage synchronized locations by clicking <i class="icon-refresh"></i> `Manage synchronization` in the <i class="icon-provider-stackedit"></i> menu. This will open a dialog box allowing you to add or remove synchronization links that are associated to your document.

> **NOTE:** If you delete the file from **Google Drive** or from **Dropbox**, the document will no longer be synchronized with that location.

----------


Publication
-----------

Once you are happy with your document, you can publish it on different websites directly from **StackEdit**. As for now, **StackEdit** can publish on **Blogger**, **Dropbox**, **Gist**, **GitHub**, **Google Drive**, **Tumblr**, **WordPress** and on any SSH server.

#### <i class="icon-share"></i> Publish a document

You can publish your document by going to the <i class="icon-share"></i> `Publish on` sub-menu and by choosing a website. In the dialog box, you can choose the publication format:

- Markdown, to publish the Markdown text on a website that can interpret it (**GitHub** for instance),
- HTML, to publish the document converted into HTML (on a blog for instance),
- Template, to have a full control of the output.

> **NOTE:** The default template is a simple webpage wrapping your document in HTML format. You can customize it in the `Services` tab of the <i class="icon-cog"></i> `Settings` dialog.

#### <i class="icon-share"></i> Update a publication

After publishing, **StackEdit** will keep your document linked to that publish location so that you can update it easily. Once you have modified your document and you want to update your publication, click on the <i class="icon-share"></i> button in the navigation bar.

> **NOTE:** The <i class="icon-share"></i> button is disabled when the document has not been published yet.

#### <i class="icon-share"></i> Manage document publication

Since one document can be published on multiple locations, you can list and manage publish locations by clicking <i class="icon-share"></i> `Manage publication` in the <i class="icon-provider-stackedit"></i> menu. This will open a dialog box allowing you to remove publication links that are associated to your document.

> **NOTE:** In some cases, if the file has been removed from the website or the blog, the document will no longer be published on that location.

----------


Markdown Extra
--------------

**StackEdit** supports **Markdown Extra**, which extends **Markdown** syntax with some nice features.

> **Tip:** You can disable any **Markdown Extra** feature in the `Extensions` tab of the <i class="icon-cog"></i> `Settings` dialog.


### Tables

**Markdown Extra** has a special syntax for tables:

Item      | Value
--------- | -----
Computer  | 1600 USD
Phone     | 12 USD
Pipe      | 1 USD

You can specify column alignment with one or two colons:

| Item      |    Value | Qty  |
| :-------- | --------:| :--: |
| Computer  | 1600 USD |  5   |
| Phone     |   12 USD |  12  |
| Pipe      |    1 USD | 234  |


### Definition Lists

**Markdown Extra** has a special syntax for definition lists too:

Term 1
Term 2
:   Definition A
:   Definition B

Term 3

:   Definition C

:   Definition D

	> part of definition D


### Fenced code blocks

GitHub's fenced code blocks[^gfm] are also supported with **Prettify** syntax highlighting:

```
// Foo
var bar = 0;
```

> **Tip:** To use **Highlight.js** instead of **Prettify**, just configure the `Markdown Extra` extension in the <i class="icon-cog"></i> `Settings` dialog.


### Footnotes

You can create footnotes like this[^footnote].

  [^footnote]: Here is the *text* of the **footnote**.


### SmartyPants

SmartyPants converts ASCII punctuation characters into "smart" typographic punctuation HTML entities. For example:

|                  | ASCII                                    | HTML                                |
 ------------------|------------------------------------------|-------------------------------------
| Single backticks | `'Isn't this fun?'`                      | &#8216;Isn&#8217;t this fun?&#8217; |
| Quotes           | `"Isn't this fun?"`                      | &#8220;Isn&#8217;t this fun?&#8221; |
| Dashes           | `-- is an en-dash and --- is an em-dash` | &#8211; is an en-dash and &#8212; is an em-dash |


### Table of contents

You can insert a table of contents using the marker `[TOC]`:

[TOC]


### Comments

Usually, comments in Markdown are just standard HTML comments. <!-- like this -->
**StackEdit** extends HTML comments in order to produce useful, highlighted comments in the preview but not in your exported documents. <!--- This is very useful for collecting feedback in a collaborative document. -->


### MathJax
 
You can render *LaTeX* mathematical expressions using **MathJax**, as on [math.stackexchange.com][2]:

The *Gamma function* satisfying $\Gamma(n) = (n-1)!\quad\forall
n\in\mathbb N$ is via the Euler integral

$$
\Gamma(z) = \int_0^\infty t^{z-1}e^{-t}dt\,.
$$

> **Tip:** Make sure you include MathJax into your publications to render mathematical expression correctly. Your page/template should include something like: 

```
<script type="text/javascript" src="https://stackedit.io/libs/MathJax/MathJax.js?config=TeX-AMS_HTML"></script>
```

> **NOTE:** You can find more information:
>
> - about **Markdown** syntax [here][3],
> - about **Markdown Extra** extension [here][4],
> - about **LaTeX** mathematical expressions [here][5],
> - about **Prettify** syntax highlighting [here][6],
> - about **Highlight.js** syntax highlighting [here][7].

  [^stackedit]: [StackEdit](https://stackedit.io/) is a full-featured, open-source Markdown editor based on PageDown, the Markdown library used by Stack Overflow and the other Stack Exchange sites.

  [^gfm]: **GitHub Flavored Markdown** (GFM) is supported by StackEdit.


  [1]: http://i1150.photobucket.com/albums/o613/ouchxp/Primaryuserinterfaces.jpg "Primary user interfaces"
  [2]: http://math.stackexchange.com/
  [3]: http://daringfireball.net/projects/markdown/syntax "Markdown"
  [4]: https://github.com/jmcmanus/pagedown-extra "Pagedown Extra"
  [5]: http://meta.math.stackexchange.com/questions/5020/mathjax-basic-tutorial-and-quick-reference
  [6]: https://code.google.com/p/google-code-prettify/
  [7]: http://highlightjs.org/
