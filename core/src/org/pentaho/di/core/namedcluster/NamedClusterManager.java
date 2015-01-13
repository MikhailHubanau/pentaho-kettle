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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.di.core.namedcluster.model.NamedCluster;
import org.pentaho.metastore.api.IMetaStore;
import org.pentaho.metastore.api.exceptions.MetaStoreException;
import org.pentaho.metastore.persist.MetaStoreFactory;
import org.pentaho.metastore.util.PentahoDefaults;

public class NamedClusterManager implements INamedClusterManager {

  private static NamedClusterManager instance = new NamedClusterManager();
  
  private Map<IMetaStore, MetaStoreFactory<NamedCluster>> factoryMap = new HashMap<IMetaStore, MetaStoreFactory<NamedCluster>>();
  
  private NamedCluster configurationTemplate;
  
  private NamedClusterManager() {
  }

  public static INamedClusterManager getInstance() {
    return instance;
  }

  private MetaStoreFactory<NamedCluster> getMetaStoreFactory( IMetaStore metastore ) {
    if ( factoryMap.get( metastore ) == null ) {
     factoryMap.put( metastore, new MetaStoreFactory<NamedCluster>( NamedCluster.class, metastore, PentahoDefaults.NAMESPACE ) );
    }
    return factoryMap.get( metastore );
  }
  
  @Override
  public NamedCluster getClusterTemplate() {
    if ( configurationTemplate == null ) {
      configurationTemplate = new NamedCluster();
      configurationTemplate.setName(  "new cluster" );
      configurationTemplate.setNameNodeHost( "localhost" );
      configurationTemplate.setNameNodePort( 50070 );
      configurationTemplate.setHdfsHost( "localhost" );
      configurationTemplate.setHdfsPort( 50075 );
      configurationTemplate.setHdfsUsername( "user" );
      configurationTemplate.setHdfsPassword( "password" );
      configurationTemplate.setJobTrackerHost( "localhost" );
      configurationTemplate.setJobTrackerPort( 50030 );
      configurationTemplate.setOozieUrl( "http://localhost:8080/oozie" );
    }
    return configurationTemplate;
  }

  @Override
  public void setClusterTemplate( NamedCluster configurationTemplate ) {
    this.configurationTemplate = configurationTemplate;
  }
  
  
  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#create(org.pentaho.di.core.namedconfig.model.NamedConfiguration, org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public void create( NamedCluster configuration, IMetaStore metastore ) throws MetaStoreException {
    MetaStoreFactory<NamedCluster> factory = getMetaStoreFactory( metastore );
    factory.saveElement( configuration );
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#read(java.lang.String, org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public NamedCluster read( String configurationName, IMetaStore metastore ) throws MetaStoreException {
    MetaStoreFactory<NamedCluster> factory = getMetaStoreFactory( metastore );
    return factory.loadElement( configurationName );
  }

  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#update(org.pentaho.di.core.namedconfig.model.NamedConfiguration, org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public void update( NamedCluster configuration, IMetaStore metastore ) throws MetaStoreException {
    MetaStoreFactory<NamedCluster> factory = getMetaStoreFactory( metastore );
    factory.deleteElement( configuration.getName() );
    factory.saveElement( configuration );
  }  

  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#delete(java.lang.String, org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public void delete( String configurationName, IMetaStore metastore ) throws MetaStoreException {
    MetaStoreFactory<NamedCluster> factory = getMetaStoreFactory( metastore );
    factory.deleteElement( configurationName );
  }  
  
  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#list(org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public List<NamedCluster> list( IMetaStore metastore ) throws MetaStoreException {
    MetaStoreFactory<NamedCluster> factory = getMetaStoreFactory( metastore );
    return factory.getElements();
  }  
  
  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#listNames(org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public List<String> listNames( IMetaStore metastore ) throws MetaStoreException {
    MetaStoreFactory<NamedCluster> factory = getMetaStoreFactory( metastore );
    return factory.getElementNames();
  }   
  
  /* (non-Javadoc)
   * @see org.pentaho.di.core.namedconfig.INamedConfigurationManager#contains(java.lang.String, org.pentaho.metastore.api.IMetaStore)
   */
  @Override
  public boolean contains( String configurationName, IMetaStore metastore ) throws MetaStoreException {
    if ( metastore == null ) {
      return false;
    }
    for ( String name : listNames( metastore ) ) {
      if ( name.equals(configurationName ) ) {
        return true;
      }
    }
    return false;
  }
  
}