# IntelliJ installation

The [IntelliJ IDE Ultimate edition](https://www.jetbrains.com/idea/)
can be used with the example. The IntelliJ Ultimate edition is
required due to its support for Spring Boot and JSP view pages.

**Step 1.** Download or clone the
[code-exaples-java](https://github.com/docusign/code-examples-java)
   repository

**Step 2.** Start IntelliJ Ultimate and choose the **Import Project** option.

![IntelliJ Import project](install_fig_1.png)

**Step 3.** Use the popup file chooser to select the
**code-examples-java** directory.

**Step 4.** The **Import Project** wizard will open. It's a
series of screens. On the first screen, select
**Import project from external model** and **Maven**.

![IntelliJ Import Maven project](install_fig_2.png)

**Step 5.** Click **Finish** and the project will
be displayed in the IDE.

## Configuring the project
Configure the example as discussed in the repository's Readme.

## Running the example

### Configuring the IDE's *Run/Debug Configuration*
IntelliJ uses **Run/Debug Configurations** to manage
settings for running the example.

**One time:** setup a Run/Debug Configuration for the example:

Step 1. Use the menu command **Run / Edit configurations...**
to open the configuration manager.

Step 2. Click the **+** (plus) sign to add a new configuration.
The configuration type is **Spring Boot**. You may need to
open the additional templates section of the template chooser.

Step 3. Update the form with the **Name** of the
configuration and the **Main class** for the configuration,
`com.docusign.App` **Tip:** use the **...** (ellipses) button next to the field to choose the Main class.

Tip: check the **Enable debug output** checkbox.

![Configure a Run configuration](install_fig_7.png)

### Running or debugging the example

Use a **Run** menu option to run or debug the example.

After the application is ready for requests, open your
browser to http://localhost:8080
