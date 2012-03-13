/*
 * Copyright 2012 Seitenbau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seitenbau.jenkins.plugins.dynamicparameter;

import hudson.Extension;
import hudson.model.ParameterValue;
import hudson.model.StringParameterValue;
import net.sf.json.JSONObject;

import org.jvnet.localizer.ResourceBundleHolder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/** Text parameter, with dynamically generated default value. */
public class StringParameterDefinition extends ParameterDefinitionBase
{
  /** Serial version UID. */
  private static final long serialVersionUID = 3162331168133114084L;

  /**
   * Constructor.
   * @param name parameter name
   * @param script script, which generates the parameter value
   * @param description parameter description
   * @param uuid identifier (optional)
   * @param remote execute the script on a remote node
   */
  @DataBoundConstructor
  public StringParameterDefinition(final String name, final String script,
      final String description, final String uuid, final boolean remote)
  {
    super(name, script, description, uuid, remote);
  }

  /** @return the default value generated by the script or {@code null} */
  public final String getDefaultValue()
  {
    final Object value = getValue();
    if (value == null)
    {
      return null;
    }
    return value.toString();
  }

  @Override
  public final ParameterValue createValue(final StaplerRequest req,
      final JSONObject jo)
  {
    final StringParameterValue v = req.bindJSON(StringParameterValue.class, jo);
    v.setDescription(getDescription());
    return v;
  }

  @Override
  public final ParameterValue createValue(final StaplerRequest req)
  {
    final String[] values = req.getParameterValues(getName());

    if (values == null)
    {
      return getDefaultParameterValue();
    }
    else if (values.length == 1)
    {
      return new StringParameterValue(getName(), values[0], getDescription());
    }
    else
    {
      throw new IllegalArgumentException(
          "Illegal number of parameter values for " + getName() + ": "
              + values.length);
    }
  }

  /** Parameter descriptor. */
  @Extension
  public static class DescriptorImpl extends ParameterDescriptor
  {
    @Override
    public final String getDisplayName()
    {
      return ResourceBundleHolder.get(StringParameterDefinition.class).format(
          "DisplayName");
    }
  }
}
