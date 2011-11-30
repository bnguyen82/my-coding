/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myviewer;

import java.util.Collection;
import org.myorg.myapi.Event;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.myorg.myviewer//MyViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "MyViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.myorg.myviewer.MyViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_MyViewerAction",
preferredID = "MyViewerTopComponent")
public final class MyViewerTopComponent extends TopComponent implements LookupListener{

    public MyViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MyViewerTopComponent.class, "CTL_MyViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(MyViewerTopComponent.class, "HINT_MyViewerTopComponent"));

    }
    private Lookup.Result<Event> result = null;

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(Event.class);
        result.addLookupListener (this);
    }
    
    @Override
    public void componentClosed() {
        result.removeLookupListener (this);
        result = null;
    }
    
    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        Collection<? extends Event> allEvents = result.allInstances();
        if (!allEvents.isEmpty()) {
            Event event = allEvents.iterator().next();
            jLabel1.setText(Integer.toString(event.getIndex()));
            jLabel2.setText(event.getDate().toString());
        } else {
            jLabel1.setText("[no selection]");
            jLabel2.setText("");
        }
    }
      

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MyViewerTopComponent.class, "MyViewerTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(MyViewerTopComponent.class, "MyViewerTopComponent.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(256, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(220, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables


    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
