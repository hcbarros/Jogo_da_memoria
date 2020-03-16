/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.UnsupportedEncodingException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class Frame extends JFrame {
	
        private static final long serialVersionUID = 2L;
        private JComboBox comboBox = null;
        private JButton botaoInicio = null;
        private int nivel = 1;
        private int erros = 0;
        private JLabel labelErros = null;
        private JLabel maximoErros = null;
        private String mensagemFinal = "";
        private static List<File> imagens = null;
        
        
        public Frame() {
                        
            inicializar();            
        }
        
        private void dificuldade(ActionEvent evt) {
            nivel = Integer.parseInt(comboBox.getModel().getSelectedItem().toString());            
        }
        
        private void iniciar(ActionEvent evt, List<File> list) {
            
            this.getContentPane().removeAll();
            inicializar();

            imagens = list;
            
            new Jogo(this);
            labelErros.setVisible(true);
            maximoErros.setVisible(true);            
        }
                      
        
        public JComboBox getCombo() {            
            return comboBox;
        }
       
        public JButton getBotaoInicio(){
            return botaoInicio;
        }
        
        public int getNivel() {
            return nivel;
        }
                    
        public int getErros() {
            return erros;
        }
        
        public void setNivel(int nivel) {
            this.nivel = nivel;
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
        
        public static List<File> getImagens() {
            return imagens;
        }
        
                
        private void inicializar(){
            
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int altura = (int)this.getToolkit().getScreenSize().getHeight();
            int largura = (int)this.getToolkit().getScreenSize().getWidth();
            
            this.setSize(largura, altura);
            this.setTitle(utfToIso("Jogo da memória"));
            
            JPanel contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(null);
       
            contentPane.setBackground(Color.WHITE);
            this.setContentPane(contentPane);
                
            this.setVisible(true);
                       
            if(comboBox == null) {
	         
            	comboBox = new JComboBox();
	            comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}));
	            comboBox.addActionListener((ActionEvent evt) -> {
	                dificuldade(evt);
	            });
            }
	            contentPane.add(comboBox);  
	            comboBox.setBounds(120, 0, 70, 30);
	            comboBox.setSelectedIndex(nivel-1);
            
            
            botaoInicio = new JButton("Iniciar");                         
            contentPane.add(botaoInicio);
            botaoInicio.setBounds(230, 0, 100, 30);    
            botaoInicio.addActionListener((ActionEvent evt) -> {
                                
                if(!imagens.isEmpty()) { 
                    
                    int retorno = JOptionPane.showConfirmDialog(null, utfToIso("Deseja usar as imagens que você salvou?"), null, 0, 1);
                    if(retorno == 0) { 
                        if(imagens.size() < 6) {
                           JOptionPane.showMessageDialog(null, "Adicionar no minimo 6 imagens!");
                           return;
                        }
                    }
                    else {
                        imagens.clear();
                        comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}));
                    }
                }
                
                iniciar(evt, imagens);
            });    
            
            if(imagens == null) imagens = new ArrayList<>();
            
            JButton addImagem = new JButton("Adicionar imagem");                         
            contentPane.add(addImagem);
            addImagem.setBounds(800, 0, 160, 30);    
            addImagem.addActionListener((ActionEvent evt) -> {
              
               JFileChooser fileChooser = new JFileChooser();
               
               fileChooser.setDialogTitle("Selecionar imagem");
               fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
               
               FileNameExtensionFilter filter;
               filter = new FileNameExtensionFilter("Imagem","png","jpg","jpeg","gif","svg","tiff","tif", "jfif","bmp");
               
               fileChooser.setFileFilter(filter);
               int retorno = fileChooser.showOpenDialog(this);
         
               ImageIcon icon = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
               icon.setImage(icon.getImage().getScaledInstance(200, 200, 0));
               
               File imagem = imagens.stream()
                       .filter(x -> x.getName().equals(fileChooser.getSelectedFile().getName()))
                       .findFirst().orElse(null);
                       
               if(imagem != null) {
                   
                   JOptionPane.showMessageDialog(null, utfToIso("A imagem já existe! Escolha outra."));
                   return;
               }
               
               retorno = JOptionPane.showConfirmDialog(null,"Salvar essa imagem?",
                       fileChooser.getSelectedFile().getName(), 0, 0, icon); 
                              
               if(retorno == 0) {
                                      
                   if(imagens.size() < 16) imagens.add(fileChooser.getSelectedFile());
                   else {
                       
                       JOptionPane.showMessageDialog(null, "Máximo de 16 imagens!");
                       return;
                   }
               
                   comboBox.setSelectedIndex(0);
                   definirNiveis();
               }
                
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
            
            JLabel l = new JLabel(utfToIso(mensagemFinal));
            Font f = new Font("Bitstream Charter Bold Italic", Font.BOLD, 40);
            l.setFont(f);
            this.getContentPane().add(l);
            l.setBounds(400, 300, 500, 100);            
        }
        
        public void definirNiveis() {
            
            if(imagens.size() > 0 && imagens.size() <= 6)
            comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2"}));
            if(imagens.size() > 6 && imagens.size() <= 9)
            comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4"}));
            if(imagens.size() > 9 && imagens.size() <= 12)
            comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6"}));
            if(imagens.size() > 12 && imagens.size() <= 16)
            comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}));            
        }
        
        public static String utfToIso(String mensagem){
            
        	String retorno = "";
            byte[] iso;
            try {
                iso = new String(mensagem.getBytes(), "UTF-8").getBytes("ISO-8859-1");

                retorno = new String(iso);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }

            return retorno;
        }   
        
}
