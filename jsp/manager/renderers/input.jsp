<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<div style="margin-bottom: 50px">
    <h2>The second situation: give me input</h2>
    
    <ul>
        <li><a href="#introduction">What is "input" exactly?</a></li>
        <li><a href="#basics">But let's start with the basics</a></li>
        <li><a href="#validator">Let's validate the input before you do more harm</a></li>
        <li><a href="#define-validator">What is a validator?</a></li>
        <li><a href="#create">How do we create new objects?</a></li>
        <li><a href="#hidden">And if the user does not provide enough input?</a></li>
    </ul>
</div>

<h3>What is "input" exactly?</h3>
<a name="introduction"></a>

<p>
    In the renderers' context "input" has a more specific meaning than normal. What we refer as input
    corresponds to a web interaction were the user is editing slots of a given object. The detail here
    is that the user is always, conceptually, editing an existing object. So gathering semi-unrelated
    pieces of data from the user to execute an action and show some result back cannot be done
    in exactly the same way.
</p>

<p>
    Is this a limitation? No. It's just done differently &#0151; maybe in a more complex way for simplest 
    cases &#0151; with the intention of making it quick and simple to edit domain objects and interact with 
    complex input from the user.
</p>

<h3>But let's start with the basics</h3>
<a name="basics"></a>

<p>
    The main difference from all the previous examples is that for input we use a different tag: <code>edit</code>.
    As the name may sugest it allows you the create a generic presentation to edit an object. Changes in
    objects are important when they are persisted. This only happens directly for domain objects so lets
    start editing some slots of a person:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>&lt;schema name=&quot;person.simple-edit&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;&gt;
        &lt;property name=&quot;size&quot; value=&quot;50&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;gender&quot;/&gt;
    &lt;slot name=&quot;idDocumentType&quot;/&gt;
    &lt;slot name=&quot;numeroDocumentoIdentificacao&quot;/&gt;
    &lt;slot name=&quot;dataValidadeDocumentoIdentificacao&quot;&gt;
        &lt;property name=&quot;size&quot; value=&quot;12&quot;/&gt;
        &lt;property name=&quot;maxLength&quot; value=&quot;10&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;availablePhoto&quot;/&gt;
&lt;/schema&gt;</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:edit id=&quot;input&quot; name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-edit&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:edit&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
             <fr:edit id="input" name="UserView" property="person" schema="person.simple-edit">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,,"/>
                </fr:layout>
            </fr:edit>
        </div>
    </div>
</div>

<p>
    You can try and change the name. Change the last name, submit and you can observe the change in general navigation bar
    were it shows the first and last name of the current user (the one we are editing).
</p>

<h3>Let's validate the input before you do more harm</h3>
<a name="validator"></a>

<p>
    With the previous form you can already introduce some very strange data. The only things that are verified
    are the database constraints. If you specify a value that violates some contraint for a column then you will
    get an error that is not handled by the renderers.
    
    There are some exceptions that do not produce a crash but instead make the submission invalid. Currently this
    is the case of exceptions that are raised when a value is to be converted from the given input to the required
    type.
</p>

<p>
    To avoid some of the problems you can include validators to each slot with the assumption that each slot value
    is represented by one input field. Validators are also declared in schemas. In each slot you can set the attribute
    <code>validator</code> with the name of the validator class. A validator will automatically be created and
    associated to the field. You will notice that the default presentation for a person input saves and extra column 
    for validators to display themselves.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>&lt;schema name=&quot;person.simple-edit-validated&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;&gt;
        &lt;validator class=&quot;net.sourceforge.fenixedu.renderers.validators.RegexpValidator&quot;&gt;
            &lt;property name=&quot;regexp&quot; value=&quot;\p{Space}*[^ ]+\p{Space}+[^ ]+.*&quot;/&gt;
            &lt;property name=&quot;message&quot; value=&quot;Escreva pelo menos o primeiro e �ltimo nome&quot;/&gt;
            &lt;property name=&quot;key&quot; value=&quot;false&quot;/&gt;
        &lt;/validator&gt;
        &lt;property name=&quot;size&quot; value=&quot;50&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;gender&quot; validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;idDocumentType&quot; validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;numeroDocumentoIdentificacao&quot; validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;dataValidadeDocumentoIdentificacao&quot;&gt;
        &lt;validator class=&quot;net.sourceforge.fenixedu.renderers.validators.RegexpValidator&quot;&gt;
            &lt;property name=&quot;regexp&quot; value=&quot;\p{Digit}?\p{Digit}/\p{Digit}?\p{Digit}/(\p{Digit}\p{Digit})?\p{Digit}\p{Digit}&quot;/&gt;
        &lt;/validator&gt;
        &lt;property name=&quot;size&quot; value=&quot;12&quot;/&gt;
        &lt;property name=&quot;maxLength&quot; value=&quot;10&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;availablePhoto&quot;/&gt;
&lt;/schema&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:edit id="validate" name="UserView" property="person" schema="person.simple-edit-validated">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,,"/>
                </fr:layout>
            </fr:edit>
        </div>
    </div>
</div>

<h3>What is a validator?</h3>
<a name="define-validator"></a>

<p>
    A validator is a simple <code>HtmlComponent</code>. This means that, among other things, it's it can be included
    by the renderer in the generated structure like any other component. By default the presentation of a validator is
    it's error message but obviously you can override that.
</p>

<p>
    How hard is it to create a new validator? Let's see an example:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>
public class RegexpValidator extends HtmlValidator {

    private String regexp;

    /**
     * Required constructor.
     */
    public RegexpValidator(Validatable component) {
        this(component, ".*");
    }

    public RegexpValidator(Validatable component, String regexp) {
        super(component);

        setRegexp(regexp);
        
        // default messsage
        setKey(true);
        setMessage("validator.regexp");
    }

    public String getRegexp() {
        return this.regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    @Override
    protected String getResourceMessage(String message) {
        return RenderUtils.getResourceString(message, getRegexp());
    }

    @Override
    public void performValidation() {
        String text = getComponent().getValue();

        setValid(text.matches(getRegexp()));
    }

}
    </pre>
</div>

<p>
    The first constructor is required to create the validator dinamically from the settings in schemas.
    The second constructor is used from renderers, that is, when the validator is always included
    by the renderer. The getters and setters allow you to specify the <code>regexp</code> from the
    schema's definition. The <code>getResourceMessage</code> was overriden to allow the resource string 
    to display the <code>regexp</code> used. The main method, <code>performValidation</code> does
    what you would expect: gets the text from the component beeing validated, and sets this validator
    valid only if the component value mathes the regular expression.
</p>

<h3>How do we create new objects?</h3>
<a name="create"></a>

<p>
    Currently, only domain objects can be created, that is, when you submit the presented form, and
    the form is valid, a new instance of the domain object is created and persisted in the database.
    Apart of that, the object creation proceeds in the same way as the previous examples.
</p>

<p>
    Once again, the main difference is that you need to use a third tag: the tag <code>create</code>.
    With the <code>create</code> tag you only specify the type of the object to create but we
    want to create a new intance and not edit an existing one.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>&lt;schema name=&quot;person.create-minimal&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;&gt;
        &lt;validator class=&quot;net.sourceforge.fenixedu.renderers.validators.RegexpValidator&quot;&gt;
            &lt;property name=&quot;regexp&quot; value=&quot;\p{Space}*[^ ]+\p{Space}+[^ ]+.*&quot;/&gt;
            &lt;property name=&quot;message&quot; value=&quot;Escreva pelo menos o primeiro e �ltimo nome&quot;/&gt;
            &lt;property name=&quot;key&quot; value=&quot;false&quot;/&gt;
        &lt;/validator&gt;
        &lt;property name=&quot;size&quot; value=&quot;50&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;username&quot; default=&quot;pxxxxx&quot;
          validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;idDocumentType&quot; default=&quot;IDENTITY_CARD&quot;
          validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;numeroDocumentoIdentificacao&quot; validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;gender&quot; validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;maritalStatus&quot; default=&quot;UNKNOWN&quot;
          validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;/&gt;
    &lt;slot name=&quot;isPassInKerberos&quot;/&gt;
&lt;/schema&gt;</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:create id=&quot;create&quot; type=&quot;net.sourceforge.fenixedu.Person&quot; schema=&quot;person.create-minimal&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:create&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
            <fr:create id="create" type="net.sourceforge.fenixedu.domain.Person" schema="person.create-minimal">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,,"/>
                </fr:layout>
            </fr:create>
        </div>
    </div>
</div>

<p>
    You can now submit the form and if everything validates a new person will be created in the
    database. When the page is redisplayed all the values inserted are present in the form but
    you are still creating objects. Not editing the object that was created. This happens because
    the viewstate is preserved and the form presentation is associated with that viewstate.
</p>

<p>
    As you may have noticed, all fields appear blank at first. This happens because we don't have 
    an instance to provide the values, so new default values are presented. Nevertheless you can 
    control part of this behaviour directly from the schema.
</p>

<p>
    The slot's attribute <code>default</code> can be used to specify the default value for the
    slot when no value is available, as in this case. The specified value will be converted to
    the type of the slot and then presented. From example, for a enumeration you can specify as
    the default value the name of the enum. Here is an example with some default values:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>(...)
    &lt;slot name=&quot;username&quot; default=&quot;pxxxxx&quot; (...)
    &lt;slot name=&quot;idDocumentType&quot; default=&quot;IDENTITY_CARD&quot; (...)
    &lt;slot name=&quot;maritalStatus&quot; default=&quot;UNKNOWN&quot; (...)
(...)</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
            <fr:create id="defaults" type="net.sourceforge.fenixedu.domain.Person" schema="person.create-minimal-defaults">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,,"/>
                </fr:layout>
            </fr:create>
        </div>
    </div>
</div>

<h3>And if the user does not provide enough input?</h3>
<a name="hidden"></a>

<p>
    Sometimes you want the user to fill some of the object's slots and want to fill the remaining slots yourself. Or
    some slots are dependant of the context. For example, if you are editing some structure and want to create a child,
    then the parent may be already determined from the context were the child is beeing created. So the user provides
    basic information for the child and you provide the parent automatically.
</p>

<p>
    It's important to notice that I am not talking about creating the object and then set the value sof the slots that
    the user didn't provided. The situation here beeing described is the user submiting all the required information
    without having the capability to edit some of them. The techinque used is similar to the hidden fields techinque. 
    The only difference is that those values are associated with slots.
</p>

<p>
    Let's suppose we wan't to create a person and automatically attribute the same roles that the current user has.
    We would do this:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:create id=&quot;hidden&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot; schema=&quot;person.create-minimal-defaults&quot;&gt;
    &lt;fr:hidden slot=&quot;personRoles&quot; multiple=&quot;true&quot; name=&quot;UserView&quot; property=&quot;person.personRoles&quot;/&gt;
    
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:create&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
            <fr:create id="hidden" type="net.sourceforge.fenixedu.domain.Person" schema="person.create-minimal-defaults">
                <fr:hidden slot="personRoles" multiple="true" name="UserView" property="person.personRoles"/>
                
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,,"/>
                </fr:layout>
            </fr:create>
        </div>
    </div>
</div>

<p>
    As you can see, we declare an hidden slot and say were the value is located in the same way as possible in the main
    tags. You can specify the name, property and scope to look for the value. You can also specify a conversion for the
    value. This is needed because every object will be translated to a string and the default conversion may not suffice
    for all cases. If the value is a <code>DomainObject</code> then a conversion is automatically provided.
</p>

<p>
    Another important, but easy to miss, aspect of the example is the <code>multiple</code> attribute. This attribute
    indicates that the value is to be translated into a list, that is, that several values are, in fact, beeing
    provided for the same slot. If <code>multiple</code> was not provided then the slot would be set with a single
    value ... unless tha value was in fact a list. This is complicated and you have several possiblities to specify 
    an hidden slot and how it should be handled. 
</p>

<p>
    Basically, if the given value is a <code>Collection</code> then <code>multiple</code> is assumed to be <code>true</code>
    for that slot. If you provide multiple declarations of the same slot then <code>multiple</code> is also set to
    <code>true</code>. In all the other cases <code>multiple</code> is considered to be <code>false</code> unless
    explicitly specified to be <code>true</code>. So, in the previous example the use of <code>multiple</code> was
    unnecessary. You can also verify that the example may not even work because the roles are set with an unspecified
    order. You can provide a specific order by examplicity give multiple values for the slot.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:create id=&quot;hidden&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot; schema=&quot;person.create-minimal-defaults&quot;&gt;
    &lt;fr:hidden slot=&quot;personRoles&quot; name=&quot;UserView&quot; property=&quot;person.personRoles[1]&quot;/&gt;
    &lt;fr:hidden slot=&quot;personRoles&quot; name=&quot;UserView&quot; property=&quot;person.personRoles[0]&quot;/&gt;
    
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:create&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
            <fr:create id="hidden" type="net.sourceforge.fenixedu.domain.Person" schema="person.create-minimal-defaults">
                <fr:hidden slot="personRoles" name="UserView" property="person.personRoles[1]"/>
                <fr:hidden slot="personRoles" name="UserView" property="person.personRoles[0]"/>
                
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,,"/>
                </fr:layout>
            </fr:create>
        </div>
    </div>
</div>

<p>
    <strong>Note</strong>: this last example probably won't work either because the Person role may not be included or
    be in the right order.
</p>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
    <span style="float: right;">
        Next: <html:link page="/renderers/actions.do">The third situation: renderers meet actions</html:link>
    </span>
</p>

