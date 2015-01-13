/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.core.namedcluster;

import java.util.List;

import org.pentaho.di.core.namedcluster.model.NamedCluster;
import org.pentaho.metastore.api.IMetaStore;
import org.pentaho.metastore.api.exceptions.MetaStoreException;

public interface INamedClusterManager {

  /**
   * This method returns the complete list of configuration templates, these are NamedConfiguration objects
   * which are loaded with all of the settings a shim or other config type requires and contains hints about
   * either their required types or default value(s).
   * 
   * Note that the list returned should be expected to be immutable and the configurations that are provided are
   * clones (deep).
   * 
   * @return the NamedCluster template
   */
  public NamedCluster getClusterTemplate();

  /**
   * This method will return a copy (clone) of the cluster template, so as to not allow the original
   * to be modified.
   * 
   * @param configuration the configuration to set
   */
  public void setClusterTemplate( NamedCluster configuration );

  /**
   * Saves a named configuration in the provided IMetaStore
   * 
   * @param configuration the configuration to save
   * @param metastore the IMetaStore to operate with
   * @throws MetaStoreException
   */
  public void create( NamedCluster configuration, IMetaStore metastore ) throws MetaStoreException;
  
  /**
   * Reads a NamedConfiguration from the provided IMetaStore
   * 
   * @param configurationName the name of the configuration to load
   * @param metastore the IMetaStore to operate with
   * @return the NamedConfiguration that was loaded
   * @throws MetaStoreException
   */
  public NamedCluster read( String configurationName, IMetaStore metastore ) throws MetaStoreException;
  
  /**
   * Updates a NamedConfiguration in the provided IMetaStore
   * 
   * @param configuration the configuration to update
   * @param metastore the IMetaStore to operate with
   * @throws MetaStoreException
   */
  public void update( NamedCluster configuration, IMetaStore metastore ) throws MetaStoreException;
  
  /**
   * Deletes a NamedConfiguration from the provided IMetaStore
   * 
   * @param configuration the configuration to delete
   * @param metastore the IMetaStore to operate with
   * @throws MetaStoreException
   */
  public void delete( String configurationName, IMetaStore metastore ) throws MetaStoreException;
  
  /**
   * This method lists the NamedConfigurations in the given IMetaStore
   * 
   * @param metastore the IMetaStore to operate with
   * @return the list of NamedConfigurations in the provided IMetaStore
   * @throws MetaStoreException
   */
  public List<NamedCluster> list( IMetaStore metastore ) throws MetaStoreException;
  
  /**
   * This method returns the list of configuration names in the IMetaStore
   * 
   * @param metastore the IMetaStore to operate with
   * @return the list of configuration names (Strings)
   * @throws MetaStoreException
   */
  public List<String> listNames( IMetaStore metastore ) throws MetaStoreException;
  
  /**
   * This method checks if the configuration exists in the metastore
   * 
   * @param configurationName the name of the configuration to check
   * @param metastore the IMetaStore to operate with
   * @return true if the configuration exists in the given metastore
   * @throws MetaStoreException
   */
  public boolean contains( String configurationName, IMetaStore metastore ) throws MetaStoreException;
}