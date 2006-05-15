<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<div style="margin-bottom: 50px">
    <h2>The third situation: renderers meet actions</h2>
    
    <ul>
        <li><a href="#wrapper">How do I collect random pieces of information?</a></li>
        <li><a href="#example">Can you give an example?</a></li>
        <li><a href="#nested">Can I still used hidden fields technique?</a></li>
        <li><a href="#controllers">Intercepting the submission and changing the destination</a></li>
        <li><a href="#attributes">Controllers and renderer's can save it's own attributes in the <code>ViewState</code></a></li>
    </ul>
</div>

<h3>How do I collect random pieces of information?</h3>
<a name="wrapper"></a>

<p>
   When you use the <code>edit</code> tag you are always editing an object.
   So you must wrap all the pieces of information in a bean and then edit
   that bean.
</p>

<h3>Can you give an example?</h3>
<a name="example"></a>

<p>
    Suppose you, for some strange reason, need the user to introduce two ages,
    a date, and a gender to search for persons. You could create the following bean:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>public class SearchBean implements Serializable {
    private int minAge;
    private int maxAge;
    private Date date;
    private Gender gender;

    public int getMinAge() {
        return this.minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}</pre>
</div>

<p>
    And in some action you create the bean and put it in a request attribute.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>SearchBean bean = new SearchBean();
bean.setDate(new Date());

request.setAttribute("bean", bean);</pre>
</div>

<p>
    But how do we send the input to the action that will search the persons? For that
    you use the <code>action</code> attribute of the <code>edit</code> tag. This attribute
    makes the form submit the content to the specified action. By default the form is
    submited to the input location, that is, the url originally requested.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;fr:edit name=&quot;bean&quot; action=&quot;/renderers/searchPersons.do?method=search&quot;/&gt;</pre>
</div>

<p>
    Now we need to know how, in the target action, we get access to the bean. Between
    interactions with the user the view state is mantained. Is through that view state
    the we can get the object that was edited. So in the action we would do something like:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>ViewState viewState = (ViewState) RenderUtils.getViewState();
SearchBean bean = (SearchBean) viewState.getMetaObject().getObject();

// search person
(...)</pre>
</div>

<p>
    All we need now is the <html:link page="/renderers/searchPersons.do?method=prepare">working example</html:link>.
    What I mean by "working" is that the pieces of code presented are executed and not that it will search
    persons.
</p>

<h3>Can I still used hidden fields technique?</h3>
<a name="nested"></a>

<p>
    Yes!
</p>

<p>
    What you need to do is used the <code>nested</code> attribute of the <code>edit</code> or <code>create</code>
    tag. If you specify <code>nested=true</code> then you can put the tag inside an existing form and consequently
    you can use hidden fields as before because you now control the surrounding form.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;html:form action=&quot;/renderers/searchPersons.do?method=search&quot;&gt;
    &lt;html:hidden property=&quot;theProperty&quot; value=&quot;theValue&quot;/&gt;
    
    &lt;fr:edit nested=&quot;true&quot; name=&quot;bean&quot; action=&quot;/renderers/searchPersons.do?method=search&quot;/&gt;
&lt;/html:form&gt;</pre>
</div>

<p>
    The outter form now defines the target action but if some controler chooses to send the flow to other destination
    they will still override the destination specified in the form.
</p>

<h3>Intercepting the submission and changing the destination</h3>
<a name="controllers"></a>

<p>
    Now imagine that you want to do something to the submited data before the action is executed or even choose
    the destination according to the user input. How can you do that? For this type of control you need to
    use controllers.
</p>

<p>
    Controllers can be associated to form components and are executed just after the components are updated
    with the submited values. Each controller has access to the corresponding controlled component and to
    the view state. This way they can influence the global structure or aspect of what is presented, control
    the lifecycle of the components, redirect to another location, etc.
</p>

<p>
    Controllers can only be associated to components by renderers and not from the configuration like 
    validators. So if you need to include controllers you probably need to create a new renderer. Here is an
    example of a controller that redirects to different locations according to gender:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class ChooseFromGenderController extends HtmlController {

    @Override
    public void execute(IViewState viewState) {
        HtmlMenu genderMenu = (HtmlMenu) getControlledComponent();
        
        Gender gender = (Gender) genderMenu.getConvertedValue(Gender.class);
        if (Gender.MALE.equals(gender)) {
            viewState.setCurrentDestination("male");
        }
        else {
            viewState.setCurrentDestination("female");
        }
    }
    
}</pre>
</div>

<p>
    Now there is something there that needs explaining. Were do we define those names in <code>setCurrentDestination</code>?
    The <code>edit</code> tag supports an inner tag named <code>destination</code> that allows you to define destinations,
    or exit point, from the generated form. So the use of the <code>edit</code> tag would be a little different:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;fr:edit name=&quot;bean&quot; action=&quot;/renderers/searchPersons.do?method=search&quot;&gt;
    &lt;fr:destination name=&quot;female&quot; path=&quot;/renderers/searchFemales.&quot;/&gt;
    &lt;fr:destination name=&quot;male&quot; path=&quot;/renderers/searchMales.do&quot;/&gt;
&lt;/fr:edit&gt;</pre>
</div>

<p>
    There is also a default behaviour that is important to know. By default there are three destination names
    that are used if provided:
</p>

<dl>
  <dt><code>"success"</code></dt><dd>is used if the submission is valid and has no errors</dd>
  <dt><code>"invalid"</code></dt><dd>is used when some validation fails</dd>
  <dt><code>"cancel"</code></dt><dd>is used when the cancel button is pressed</dd>
</dl>

<p>
    The next example shows a controller that capitalizes the text of a field. This controller needs to
    be associated to a submit button to be executed properlly. The behaviour inherited from 
    <code>HtmlSubmitButtonController</code> makes this controller be executed only when the button is
    pressed and changes the submission lifecycle to not alter the object.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class CapitalizeController extends HtmlSubmitButtonController {

    private HtmlSimpleValueComponent input;
    
    public CapitalizeController(HtmlSimpleValueComponent component) {
        this.input = component;
    }

    @Override
    protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
        String text = this.input.getValue();
        this.input.setValue(capitalize(text));
    }

    private String capitalize(String text) {
        StringBuilder buffer = new StringBuilder();
        char ch, prevCh;
        
        prevCh = ' ';
        for (int i = 0;  i &lt; text.length();  i++) {
           ch = text.charAt(i);
           
           if (Character.isLetter(ch)  &amp;&amp;  !Character.isLetter(prevCh)) {
               buffer.append(Character.toUpperCase(ch));
           }
           else {
               buffer.append(ch);
           }
           
           prevCh = ch;
        }
        
        return buffer.toString();
    }
    
}</pre>
</div>

<h3>Controllers and renderer's can save it's own attributes in the <code>ViewState</code></h3>
<a name="attributes"></a>

<p>
    Sometimes for a more complex implementation of a controller or renderer you need to store some
    values between requests of the user. The <code>ViewState</code> object allows you to set attributes
    that are persisted (in the client side) between requests. The view state attributes are global,
    that is, if you set the attribute <code>"a"</code> in one renderer then all renderers and controllers
    that are executed next will see that attribute. Nevetheless the default input context implementation
    tries make attributes local to each renderer. The objective is to avoid name conflicts and make 
    the implementation easier.
</p>

<p>
    The next example shows an additional controllers that change the value of a field to it's uppercase. 
    This controller is defined as a inner class of a renderer so it has access to the input context and
    consequently to the view state. It uses this fact to maintain the controller in the right state
    even after the values are submited and the object is changed. This controller also interacts with
    the previous controller to make the capitalize button visible only when the value is in lower case.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class CaseChangeController extends HtmlSubmitButtonController {

    private HtmlSubmitButton button;
    private HtmlSubmitButton capitalize;
    private HtmlSimpleValueComponent input;
    
    public CaseChangeController(HtmlSimpleValueComponent component, HtmlSubmitButton button, HtmlSubmitButton capitalizeButton) {
        this.input = component;
        this.button = button;
        this.capitalize = capitalizeButton;
        
        setupButtons();
    }

    private void setupButtons() {
        this.button.setText(isUpperCase() ? "To Upper Case" : "To Lower Case");
        this.capitalize.setVisible(isUpperCase());
    }
    
    private boolean isUpperCase() {
        if (getInputContext().getViewState().getAttribute("isUpperCase") == null) {
            return true;
        }
        
        return ((Boolean) getInputContext().getViewState().getAttribute("isUpperCase")).booleanValue();
    }
    
    private void setUpperCase(boolean isUpperCase) {
        getInputContext().getViewState().setAttribute("isUpperCase", new Boolean(isUpperCase));
    }
    
    @Override
    protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
        String text = this.input.getValue();
        this.input.setValue(isUpperCase() ? text.toUpperCase() : text.toLowerCase());
        
        setUpperCase(!isUpperCase());
        setupButtons();
    }
}</pre>
</div>

<p>
    To use this two new controllers we need to create a custom renderer but that will only be shown latter. 
    Right now all we need to know is that a new layout was defined and associated with the implemented
    renderer. Now lets see the working example. 
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>&lt;schema name=&quot;person.create-minimal-defaults&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot; layout=&quot;allow-case-change&quot;/&gt;
&lt;/schema&gt;</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:edit id=&quot;case-change&quot; name=&quot;UserView&quot; property=&quot;person&quot; layout=&quot;tabular&quot; schema=&quot;person.name&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
             <fr:edit id="case-change" name="UserView" property="person" layout="tabular" schema="person.name"/>
        </div>
    </div>
</div>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
    <span style="float: right;">
        Next: <html:link page="/renderers/new.do">The fourth situation: a new renderer</html:link>
    </span>
</p>
