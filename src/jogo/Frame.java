/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Frame extends JFrame {
	
        private static final long serialVersionUID = 2L;
        private JComboBox comboBox;
        private boolean inicio = false;
        private int nivel = 1;
        private int erros = 0;
        private JLabel labelErros = null;
        private JLabel maximoErros = null;
        private String mensagemFinal = "";
        
        
        public Frame() {
                        
            inicializar();            
        }
        
        private void dificuldade(ActionEvent evt) {
            nivel = Integer.parseInt(comboBox.getModel().getSelectedItem().toString());            
        }
        
        private void iniciar(ActionEvent evt) {
            
            this.getContentPane().removeAll();
            inicializar();
                        
            new Jogo(this);
            labelErros.setVisible(true);
            maximoErros.setVisible(true);
        }
                      
        
        public int getNivel() {
            return nivel;
        }
        
        public boolean getInicio() {
            return inicio;
        }
        
        public int getErros() {
            return erros;
        }
        
        public void setNivel(int nivel) {
            this.nivel = nivel;
        }
        
        public void setInicio(boolean inicio) {
            this.inicio = inicio;
        }
        
        public void setErros(){
            erros++;
        }
        
        public void setLabelErros(){
            labelErros.setText("Total de erros: "+erros);
        }
        
        public void setMensagem(String mensagemFinal){
            this.mensagemFinal = mensagemFinal;
        }
        
        public void setMaximoErros(int max){
            maximoErros.setText(maximoErros.getText()+max);
        }
        
                
        private void inicializar(){
            
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(1300,800);
            this.setTitle("Jogo da memória");
            
            JPanel contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(null);
       
            contentPane.setBackground(Color.WHITE);
            this.setContentPane(contentPane);
                
            this.setVisible(true);
                       
            comboBox = new JComboBox();
            comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}));
            comboBox.addActionListener((ActionEvent evt) -> {
                dificuldade(evt);
            });
            contentPane.add(comboBox);  
            comboBox.setBounds(120, 0, 70, 30);
            comboBox.setSelectedIndex(nivel-1);
            
            
            JButton button = new JButton("Iniciar");                         
            contentPane.add(button);
            button.setBounds(230, 0, 100, 30);    
            button.addActionListener((ActionEvent evt) -> {
              iniciar(evt);
            });    
           
            Font f = new Font("Bitstream Charter Bold Italic", Font.BOLD, 20);

            JLabel label = new JLabel("Dificuldade:");
            label.setFont(f);
            contentPane.add(label);
            label.setBounds(0, 0, 200, 30);

            erros = 0;
            labelErros = new JLabel("Total de erros: "+erros);
            labelErros.setFont(f);
            contentPane.add(labelErros);
            labelErros.setBounds(400, 0, 170, 30);
            labelErros.setVisible(false);
                        
            maximoErros = new JLabel("Erros permitidos: ");
            maximoErros.setFont(f);
            contentPane.add(maximoErros);
            maximoErros.setBounds(600, 0, 200, 30);
            maximoErros.setVisible(false);
                            
        }
        
        public void gameOver(){
            
            int err = erros;
            this.getContentPane().removeAll();
            inicializar();
            labelErros.setVisible(true);
            labelErros.setText("Total de erros: "+err);
            
            JLabel l = new JLabel(mensagemFinal);
            Font f = new Font("Bitstream Charter Bold Italic", Font.BOLD, 40);
            l.setFont(f);
            this.getContentPane().add(l);
            l.setBounds(400, 300, 500, 100);            
        }       
        
}
