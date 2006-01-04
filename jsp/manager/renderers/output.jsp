<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<div style="margin-bottom: 50px">
    <h2>The first situation: presenting the world</h2>
    
    <ul>
        <li><a href="#warming">Warming up</a></li>
        <li><a href="#present">Presenting an object</a></li>
        <li><a href="#labels">Before schemas what about ...</a></li>
        <li><a href="#schemas">And now schemas for everybody</a></li>
        <li><a href="#properties">Renderers can receive properties</a></li>
        <li><a href="#more-schemas">Schemas have so much more to be said</a></li>
    </ul>
</div>

<h3>Warming up</h3>
<a name="warming"></a>

As the use of renderers is made through a TagLib, the first thing you need to do is to 
declare the use of the <code>fenix-renderers.tld</code> description file. The following
line does the trick:

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px; margin-top: 10px; margin-bottom: 10px;" >
    <pre>&lt;%@ taglib uri=&quot;/WEB-INF/fenix-renderers.tld&quot; prefix=&quot;fr&quot; %&gt;</pre>
</div>

The TLD is somewhat documented and eclipse is able to show that documentation if you have the Web
Standard Tools installed. Just copy the <code>fenix-renderers.tld</code> to the 
<code>jsp/WEB-INF</code> directory and possibly restart eclipse. From this moment you can use the
beloved <code>Ctrl+Space</code> shortcut to auto-complete tags and see documentation.

<h3>Presenting an object</h3>
<a name="present"></a>

<p>
    The first thing you would like to know is: 

    <strong>How can I just present something and what can I present?</strong><br/>
</p>

<p>    
    The last part first. You can present any object given that you have a renderer configured to handle
    that object type. Normally there is a renderer for the <code>java.lang.Object</code> type so every
    object is presentable.
</p>

<p>
    Now for the first part. To present an object you can use the <code>view</code> tag. If you have
    Eclipse configured then you can read the documentation for the tag while using it. But the basic
    use of the tag is:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; scope=&quot;session&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" scope="session"/>
        </div>
    </div>
</div>

<p>
    As you can see, we are using the default presentation for the UserView object available in the session scope.
    If you ommit the <code>scope</code> attribute then the attribute with the given name will be searched in all
    scopes starting from the most specific, that is, page scope.
</p>

<p>
    You can also see that the default presentation of a person is simply a link to it's details. More concretly,
    it's a link to an action that presents the person in the tabular layout. You can select the layout directly
    using the <code>layout</code> attribute of the tag.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; layout=&quot;tabular&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" layout="tabular"/>
        </div>
    </div>
</div>

<p>
    You may have notice the introduction of the <code>property</code> attribute. This works together with the
    <code>name</code> attribute to further select the target object. It's supposed to behave exactly as in
    other tags available from the Struts project, so you can use a syntax like 
</p>

    <ul>
        <li><code>"person.pais.code"</code></li>
        <li>or <code>"person.students[0].number"</code></li>
    </ul>
   
<p>    
     as long as all middle elements are not null.
</p>

<h3>Before schemas what about ...</h3>
<a name="labels"></a>

<p>
    ... that user friendlly names that appear in the left side of the table?
</p>

<p>
    Glad you ask. As you won't believe in magic I'm forced to tell you that there is a new resource bundle
    in town: <code>RendererResources</code>. The text that appears in the left side is fetched from the
    resources using a simple convention. Each row correspondes to a slot of the object. For example,
    <code>nome</code> is one of the person's slots because we have the <code>getNome</code> getter. When
    the label for a slot is needed we search the renderer's resources for a key using the following order:
</p>

    <ol>
      <li><code>label.net.sourceforge.fenixedu.domain.Person.nome</code></li>
      <li><code>label.nome</code></li>
      <li><code>nome</code></li>
    </ol>

<p>
    If no key is present the the programmatic name of the slot is shown. <strong>Note</strong> that slots 
    may have more complex names like <code>pais.code</code>. This does not correspond directly to a getter
    of person but is treated the same way as before.
</p>

<p>
    ... all the other getters that a person has due to all the relations it mantains?
</p>

<p>
    For those you will have to request their display explicitly and decide how they are presented. By default
    only the direct slots of the object are displayed. In the case of domain objects, like the person here,
    those are the ones present in the <code>class</code> definition of the DML.
</p>

<p>
    And why is this so?
</p>

<p>
    Well, direct slots already gain the semantic of "data" in the DML. Relations are more related to business
    logic and domain organization than to data. Each person is related to a country. when you present a person
    you most probably want to show the slot <code>nome</code> but do you wan't to present the country? You may
    wan't to present the country's code or country's name but not all the information of the country because
    then you would have to handle and the country's relations as well. Now consider an external person. Itself
    it has no direct slots, only relations. So when you present a person you want to present it's relation
    with an external institution, an external jury, or an external guider. The need is context specific so the
    decision should also be.
</p>

<p>
    Ah! Other thing. A person has roles. Suppose that in the manager context you wan't to display the details 
    of a role, for instance, the role <code>PERSON</code>. If it's relations were shown by default then the
    manager would get, among other things, a list will several thousand items: all the active persons in the 
    database.
</p>

<h3>And now schemas for everybody</h3>
<a name="schemas"></a>

<p>
    The last example shows a lot of information some of which we may not need. We can control this by
    introducing schemas. The simple vision of schemas is that they declare which slots we are interested
    for a particular type. If, for example, we want to shown only the name, username, and email of 
    a person we could declare the following schema:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema name=&quot;person.simple-admin-info&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;/&gt;
    &lt;slot name=&quot;username&quot;/&gt;
    &lt;slot name=&quot;email&quot;/&gt;
&lt;/schema&gt;</pre>
</div>

<p>
    Schemas are refered by name so the names must be unique. To refer to a schema you can use the
    <code>schema</code> attribute in the <code>view</code> tag. The target object will be shown
    using the given schema, that is, like if it only had the slots declared in the schema.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; 
         layout=&quot;tabular&quot; schema=&quot;person.simple-admin-info&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" layout="tabular" schema="person.simple-admin-info"/>
        </div>
    </div>
</div>

<h3>Renderers can receive properties</h3>
<a name="properties"></a>

<p>
    So we now can present a person showing only the information we want and in a tabular layout as intended.
    But sometimes we need to ajust some detail in the we things are presented. One of the things that may
    vary with the context is the style tables and other information is shown. Nevertheless we could need
    other subtle changes.
</p>

<p>
    Most of these changes can be done by passing properties to the renderer. Yes you can pass properties to
    the renderers. If a renderer has methods <code>getFoo</code> and <code>setFoo</code> the it supports
    a property named <code>"foo"</code>.
</p>

<p>
    Ok. Before I show you how to set renderers properties some things must be explained. When you use the
    attribute <code>layout</code> you are actually using a sort of syntactic sugar for a more complex,
    expanded version. The expanded version of the last example is this:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
</div>

<p>
    That inner <code>layout</code> tag allows you to both select and configure the layout. To set renderer's
    properties you can user the tag <code>property</code> that i to be used inside the tag <code>layout</code>.
    The tag <code>property</code> only has two attributes, both required: name and value. And now an example:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" schema="person.simple-admin-info">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,"/>
                </fr:layout>
            </fr:view>            
        </div>
    </div>
</div>

<p>
    In this example we are setting the <code>classes</code> and <code>columnClasses</code> properties
    of the renderer. As the renderer generates a table these properties are used to costumize the
    relevant parts of it. You could also specify the properties <code>rowClasses</code> and
    <code>headerClasses</code>. Note that the value of <code>columnClasses</code> is 
    <code>"listClasses,&lt;blanck&gt;"</code> meaning that the first column as the specified class and
    the second has no class.
</p>

<h3>Schemas have so much more to be said</h3>
<a name="more-schemas"></a>

<p>
    We saw the simple use of schemas: to limit the presented slots. But schemas can contain much more 
    information for each slot. You can, for instance, indicate the layout, schema, a properties that
    will be used to present the value of a slot. This makes the renderers adopt a more recursive and
    flexible style.
</p>

<p>
    The following example extends the schema previously defined and uses it as before:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>&lt;schema name=&quot;person.simple-admin-info.extended&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;/&gt;
    &lt;slot name=&quot;username&quot; layout=&quot;link&quot;/&gt;
    &lt;slot name=&quot;email&quot;&gt;
        &lt;property name=&quot;link&quot; value=&quot;true&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;pais&quot; schema=&quot;country.name&quot; layout=&quot;values&quot;/&gt;
&lt;/schema&gt;

&lt;schema name=&quot;country.name&quot; type=&quot;net.sourceforge.fenixedu.domain.Country&quot;&gt;
    &lt;slot name=&quot;name&quot;/&gt;
&lt;/schema&gt;
</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info.extended&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" schema="person.simple-admin-info.extended">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,"/>
                </fr:layout>
            </fr:view>            
        </div>
    </div>
</div>
