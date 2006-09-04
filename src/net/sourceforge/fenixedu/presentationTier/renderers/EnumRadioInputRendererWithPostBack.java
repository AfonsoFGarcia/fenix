package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.EnumRadioInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonList;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class EnumRadioInputRendererWithPostBack extends EnumRadioInputRenderer  {

    private String destination;

    public String getDestination() {
        return destination;
    }

    /**
     * Allows to choose the postback destination. If this property is not
     * specified the default "postback" destination is used.
     * 
     * @property
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        final Layout layout = super.getLayout(object, type);
        
        return new Layout() {
            
            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlRadioButtonList radioButtonList = (HtmlRadioButtonList) layout.createComponent(object, type);
                
                radioButtonList.setController(new PostBackController(getDestination()));
                for (HtmlRadioButton button : radioButtonList.getRadioButtons()) {
                    button.setOnClick("this.form.submit();");
                }
                
                return radioButtonList;
            }

        };
    }

    private static class PostBackController extends HtmlController {

        private String destination;

        public PostBackController(String destination) {
            this.destination = destination;
        }

        @Override
        public void execute(IViewState viewState) {

            ViewDestination destination = viewState.getDestination(this.destination);

            if (destination != null) {
                viewState.setCurrentDestination(destination);
            } else {
                viewState.setCurrentDestination("postBack");
            }

            viewState.setSkipValidation(true);
        }

    }
}
