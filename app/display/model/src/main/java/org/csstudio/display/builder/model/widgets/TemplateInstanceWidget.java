/*******************************************************************************
 * Copyright (c) 2021 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.csstudio.display.builder.model.widgets;

import static org.csstudio.display.builder.model.properties.CommonWidgetProperties.propFile;
import static org.csstudio.display.builder.model.properties.CommonWidgetProperties.propHorizontal;

import java.util.List;
import java.util.Objects;

import org.csstudio.display.builder.model.DisplayModel;
import org.csstudio.display.builder.model.Messages;
import org.csstudio.display.builder.model.RuntimeWidgetProperty;
import org.csstudio.display.builder.model.Widget;
import org.csstudio.display.builder.model.WidgetCategory;
import org.csstudio.display.builder.model.WidgetDescriptor;
import org.csstudio.display.builder.model.WidgetProperty;
import org.csstudio.display.builder.model.WidgetPropertyCategory;
import org.csstudio.display.builder.model.WidgetPropertyDescriptor;
import org.csstudio.display.builder.model.properties.IntegerWidgetProperty;

/** Widget that duplicates a 'template' multiple times
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
// TODO Is VisibleWidget the best base class?
public class TemplateInstanceWidget extends VisibleWidget
{
    public static final int DEFAULT_WIDTH = 400,
                            DEFAULT_HEIGHT = 300;

    /** Widget descriptor */
    public static final WidgetDescriptor WIDGET_DESCRIPTOR =
        new WidgetDescriptor("template", WidgetCategory.STRUCTURE,
            "Template/Instance",
            "/icons/embedded.png",
            "Widget that embeds a template with multiple instances")
    {
        @Override
        public Widget createWidget()
        {
            return new TemplateInstanceWidget();
        }
    };

    /** 'gap' property: Gap between instances */
    public static final WidgetPropertyDescriptor<Integer> propGap =
        new WidgetPropertyDescriptor<>(WidgetPropertyCategory.DISPLAY, "gap", Messages.Gap)
        {
            @Override
            public WidgetProperty<Integer> createProperty(final Widget widget, final Integer value)
            {
                return new IntegerWidgetProperty(this, widget, value, 0, 500);
            }
        };

    public static final WidgetPropertyDescriptor<DisplayModel> runtimeModel =
        new WidgetPropertyDescriptor<>(WidgetPropertyCategory.RUNTIME, "embedded_model", "Embedded Model")
        {
            @Override
            public WidgetProperty<DisplayModel> createProperty(final Widget widget, DisplayModel default_value)
            {
                return new RuntimeWidgetProperty<>(runtimeModel, widget, default_value)
                {
                    @Override
                    public void setValueFromObject(final Object value)
                            throws Exception
                    {
                        if (! (value instanceof DisplayModel))
                            throw new IllegalArgumentException("Expected DisplayModel, got " + Objects.toString(value));
                        doSetValue((DisplayModel)value, true);
                    }
                };
            }
        };

    private volatile WidgetProperty<String> file;
    private volatile WidgetProperty<Boolean> horizontal;
    private volatile WidgetProperty<Integer> gap;
    private volatile WidgetProperty<DisplayModel> embedded_model;


    public TemplateInstanceWidget()
    {
        super(WIDGET_DESCRIPTOR.getType(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    protected void defineProperties(final List<WidgetProperty<?>> properties)
    {
        super.defineProperties(properties);
        properties.add(file = propFile.createProperty(this, ""));
        properties.add(horizontal = propHorizontal.createProperty(this, false));
        properties.add(gap = propGap.createProperty(this, 0));
        properties.add(embedded_model = runtimeModel.createProperty(this, null));
    }

    /** @return 'file' property */
    public WidgetProperty<String> propFile()
    {
        return file;
    }

    /** @return 'horizontal' property */
    public WidgetProperty<Boolean> propHorizontal()
    {
        return horizontal;
    }

    /** @return 'gap' property */
    public WidgetProperty<Integer> propGap()
    {
        return gap;
    }

    /** @return Runtime 'model' property for the embedded display */
    public WidgetProperty<DisplayModel> runtimePropEmbeddedModel()
    {
        return embedded_model;
    }
}
