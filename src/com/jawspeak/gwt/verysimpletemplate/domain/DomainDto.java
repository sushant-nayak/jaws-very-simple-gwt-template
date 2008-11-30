/*
 * Copyright 2008 Jonathan Andrew Wolter
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jawspeak.gwt.verysimpletemplate.domain;

import java.util.Date;

/** Domain class to represent the objects compiled into the GWT js code. 
 * This could be created automatically from your Entities, or otherwise your
 * java persistent objects with hibernate4gwt or dozer, or other bean mapping tools.
 * (Or in some cases you can directly use the persistent entities in your gwt client code.
 * However, that is outside the scope of what this project covers). 
 * 
 * @author Jonathan Andrew Wolter <jaw@jawspeak.com>
 */
public class DomainDto {
  private Date myStartDate;
  private String name;
  private Integer count;
  private Double someRatio;
  
  public Date getMyStartDate() {
    return myStartDate;
  }
  public void setMyStartDate(Date myStartDate) {
    this.myStartDate = myStartDate;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Integer getCount() {
    return count;
  }
  public void setCount(Integer count) {
    this.count = count;
  }
  public Double getSomeRatio() {
    return someRatio;
  }
  public void setSomeRatio(Double someRatio) {
    this.someRatio = someRatio;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((count == null) ? 0 : count.hashCode());
    result = prime * result + ((myStartDate == null) ? 0 : myStartDate.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((someRatio == null) ? 0 : someRatio.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DomainDto other = (DomainDto) obj;
    if (count == null) {
      if (other.count != null)
        return false;
    } else if (!count.equals(other.count))
      return false;
    if (myStartDate == null) {
      if (other.myStartDate != null)
        return false;
    } else if (!myStartDate.equals(other.myStartDate))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (someRatio == null) {
      if (other.someRatio != null)
        return false;
    } else if (!someRatio.equals(other.someRatio))
      return false;
    return true;
  }
}
