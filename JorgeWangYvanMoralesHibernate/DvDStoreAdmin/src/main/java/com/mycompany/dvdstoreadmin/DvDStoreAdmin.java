/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.dvdstoreadmin;
import java.awt.*;
import java.awt.event.*;
import java.lang.module.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
/**
 *
 * @author Jorge Wang y Yvan Morales
 */
public class DvDStoreAdmin {

    public static void main(String[] args) {
        ActorViewer actorViewer = new ActorViewer(hibernateUtil.getSessionFactory());
    }
}
 class ActorViewer extends JFrame {
    private JTextField txtFirstNameFilter;
    private JTextField txtLastNameFilter;
    private JTextArea txtResult;
    private SessionFactory sessionFactory;

    public ActorViewer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        initializeUI();
    }

    private void initializeUI() {

        setTitle("Actor Viewer");
        setSize(450, 300);
        setLayout(new FlowLayout());

        JLabel lblFirstName = new JLabel("First Name:");
        JLabel lblLastName = new JLabel("Last Name:");
        txtFirstNameFilter = new JTextField(30);
        txtLastNameFilter = new JTextField(30);
        txtResult = new JTextArea(10, 35);
        txtResult.setEditable(false);
        setResizable(false);
        add(lblFirstName);
        add(txtFirstNameFilter);
        add(lblLastName);
        add(txtLastNameFilter);
        add(new JScrollPane(txtResult));

        DocumentListener documentListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterActors(); }
            public void removeUpdate(DocumentEvent e) { filterActors(); }
            public void changedUpdate(DocumentEvent e) { filterActors(); }
        };

        txtFirstNameFilter.getDocument().addDocumentListener(documentListener);
        txtLastNameFilter.getDocument().addDocumentListener(documentListener);

        filterActors(); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
private void filterActors() {
    String firstNameFilter = txtFirstNameFilter.getText().trim();
    String lastNameFilter = txtLastNameFilter.getText().trim();
    String queryString = "FROM Actor";

    if (!firstNameFilter.isEmpty() || !lastNameFilter.isEmpty()) {
        queryString += " WHERE firstName LIKE :firstName AND lastName LIKE :lastName";
    }

    try (Session session = sessionFactory.openSession()) {
        Query<Actor> query = session.createQuery(queryString, Actor.class);
        if (!firstNameFilter.isEmpty() || !lastNameFilter.isEmpty()) {
            query.setParameter("firstName", firstNameFilter + "%");
            query.setParameter("lastName", lastNameFilter + "%");
        }
        List<Actor> actors = query.list();

        txtResult.setText("");
        for (Actor actor : actors) {
            txtResult.append(actor.getActorId() + ", " + actor.getFirstName() + " " + actor.getLastName() + "\n");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
   
}
    