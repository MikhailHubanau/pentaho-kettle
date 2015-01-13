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

package org.pentaho.di.ui.spoon.delegates;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.namedcluster.NamedClusterManager;
import org.pentaho.di.core.namedcluster.model.NamedCluster;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.namedcluster.dialog.NamedClusterDialog;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.metastore.api.IMetaStore;
import org.pentaho.metastore.api.exceptions.MetaStoreException;

public class SpoonNamedClusterDelegate extends SpoonDelegate {
  private static Class<?> PKG = Spoon.class; // for i18n purposes, needed by Translator2!!

  public SpoonNamedClusterDelegate( Spoon spoon ) {
    super( spoon );
  }

  public void dupeNamedCluster( IMetaStore metaStore, NamedCluster nc, Shell shell ) {
    if ( nc != null ) {
      NamedCluster ncCopy = nc.clone();
      String dupename = BaseMessages.getString( PKG, "Spoon.Various.DupeName" ) + nc.getName();
      ncCopy.setName( dupename );

      NamedClusterDialog namedConfigurationDialog = new NamedClusterDialog( shell , ncCopy);
      String newname = namedConfigurationDialog.open();
      
      if ( newname != null ) { // null: CANCEL
        saveNamedCluster( metaStore, ncCopy );
        spoon.refreshTree();
      }
    }
  }  
  
  public void delNamedCluster( IMetaStore metaStore, NamedCluster configuration ) {
    deleteNamedCluster( metaStore, configuration );
    spoon.refreshTree();
    spoon.setShellText();
  }
  
  public void editNamedCluster( IMetaStore metaStore, NamedCluster configuration, Shell shell ) {
    if ( metaStore == null ) {
      return;
    }
    NamedClusterDialog namedClusterDialog = new NamedClusterDialog( shell , configuration.clone() );
    String result = namedClusterDialog.open();
    if ( result != null ) {
      deleteNamedCluster( metaStore, configuration );
      saveNamedCluster( metaStore, namedClusterDialog.getNamedCluster() );
      spoon.refreshTree();
    }    
  }

  public void newNamedCluster( VariableSpace variableSpace, IMetaStore metaStore, Shell shell ) {
    if ( metaStore == null ) {
      return;
    }
    
    NamedCluster nc = NamedClusterManager.getInstance().getClusterTemplate();
    
    NamedClusterDialog namedConfigurationDialog = new NamedClusterDialog( shell , nc );
    String result = namedConfigurationDialog.open();
    
    if ( result != null ) {
      if ( variableSpace != null ) {
        nc.shareVariablesWith( (VariableSpace) variableSpace );
      } else {
        nc.initializeVariablesFrom( null );
      }
  
      saveNamedCluster( metaStore, nc );
      spoon.refreshTree();    
    }
  }
  
  private void deleteNamedCluster( IMetaStore metaStore, NamedCluster configuration ) {
    if ( metaStore != null ) {
      try {
        if ( NamedClusterManager.getInstance().read( configuration.getName(), metaStore ) != null ) {
          NamedClusterManager.getInstance().delete( configuration.getName(), metaStore );
        }
      } catch (MetaStoreException e) {
        new ErrorDialog( spoon.getShell(),
            BaseMessages.getString( PKG, "Spoon.Dialog.ErrorDeletingNamedConfiguration.Title" ),
            BaseMessages.getString( PKG, "Spoon.Dialog.ErrorDeletingNamedConfiguration.Message", configuration.getName() ), e );
      }
    }    
  }
  
  private void saveNamedCluster( IMetaStore metaStore, NamedCluster configuration ) {
    if ( metaStore != null ) {
      try {
        NamedClusterManager.getInstance().create( configuration, metaStore );
      } catch (MetaStoreException e) {
        new ErrorDialog( spoon.getShell(),
            BaseMessages.getString( PKG, "Spoon.Dialog.ErrorSavingNamedConfiguration.Title" ),
            BaseMessages.getString( PKG, "Spoon.Dialog.ErrorSavingNamedConfiguration.Message", configuration.getName() ), e );
      }
    }      
  }  
  
}
