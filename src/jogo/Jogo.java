/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 *
 * @author henrique
 */
public class Jogo {

    private Frame frame = null;
    private List<Integer> numeros = null;
    private List<Integer> clone = null;
    private List<JButton> botoes = null;
    private boolean imagemAtiva = true;
    private int imagem = 0;
    private int sizeIcon = 0;
        
    
    public Jogo(Frame meuframe) {
        
        frame = meuframe;
        frame.getBotaoInicio().setEnabled(false);
        numeros = new ArrayList<>();
        botoes = new ArrayList<>();
        int n = frame.getNivel();                             
        
        if(n == 1) dificuldade(150, 12, 1); 
        if(n == 2) dificuldade(150, 12, 2);
        if(n == 3) dificuldade(150, 18, 3);
        if(n == 4) dificuldade(150, 18, 4);
        if(n == 5) dificuldade(120, 24, 5);
        if(n == 6) dificuldade(120, 24, 6);
        if(n == 7) dificuldade(120, 30, 7);
        if(n == 8) dificuldade(120, 30, 8);
        
        frame.setMaximoErros(n == 1 || n == 3 || n == 6 || n == 8 ? 9 : 
                n == 2 || n == 4 ? 4 : 19);
    }
    
    
    private void alterar(ActionEvent evt, JButton button, int num) {
                                
            imagemAtiva = true;
            ImageIcon iconNum = getIcon(num);                        
            button.setIcon(iconNum);
            
            if(clone.contains(num) && !botoes.contains(button)) jogada(button, num); 
            else imagemAtiva = false;
            
    }
    
    public void jogo(int a, int b, int c, int d) {
                
        for(int i = 100; i <= (500 + a); i = i + (200 - a)) {
            for(int j = b; j <= c; j = j + d) {
                if(numeros.isEmpty()) break;
                JButton button = criarBotao(j, i);
                botoes.add(button);
                int num = imagem;
                button.addActionListener((ActionEvent evt) -> {
                    
                   if(!imagemAtiva) alterar(evt,button, num);                                                            
                });              
            }
        }
        
        esconderBotoes(0,2000);        
    }
        
    private JButton criarBotao(int x, int y) {
        
        imagem = numeros.remove(0);
        ImageIcon icon = getIcon(imagem);
        JButton button = new JButton(icon);
        
        button.setBorder(new LineBorder(new Color(120,0,255), 4));
        
        frame.getContentPane().add(button);
        button.setBounds(x, y, sizeIcon, sizeIcon);
        return button;
    }
    
    private ImageIcon getIcon(int x) {
       
        String imagem = "int";
        if(x > 0) imagem = Integer.toString(x);
        ImageIcon icon = new ImageIcon("src/imagens/"+imagem+".png");
        icon.setImage(icon.getImage().getScaledInstance(sizeIcon, sizeIcon, 100));
        return icon;
    }
    
     
    private void esconderBotoes(int index, int time) {
         
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {  
               try {
                  Thread.sleep(time);
               } catch (InterruptedException e1) {
                  e1.printStackTrace();
               }    
                ImageIcon icon = getIcon(0);
                JButton b = botoes.get(index);
                b.setIcon(icon);
                if(index < botoes.size() - 1) esconderBotoes(index+1,50);
                else {    
                    imagem = 0;
                    imagemAtiva = false;
                    botoes.clear();
                    frame.getBotaoInicio().setEnabled(true);
                }
            }
        });     
    }
    
    private void jogada(JButton button, int num) {
        
            botoes.add(button);
     
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {  
                    try {
                       Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                       e1.printStackTrace();
                    }    
                              
                   
                    if(imagem > 0) {
                       
                        if(imagem != num){

                            for(JButton b: botoes) b.setIcon(getIcon(0));                     
                            frame.setErros();
                            frame.setLabelErros();                            
                            
                        }

                        else //{
                            clone.removeAll(clone.stream().filter(x-> x == num)
                                .collect(Collectors.toList()));
                        
                         //DESCOMENTAR ESSAS LINHAS CASO DESEJE DESATIVAR
                         //OS BOTOES QUE TIVERAM ACERTO
                         // botoes.get(0).setEnabled(false);
                         // botoes.get(1).setEnabled(false);
                      //}                        
                        
                        definirJogo();
                        botoes.clear();
                        imagem = 0;
                    }
                                                       
                    else imagem = num;
                   
                   imagemAtiva = false;                   
                }
            });

    }
    
    private void dificuldade(int sizeIcon, int qNum, int nivel) {
        
        this.sizeIcon = sizeIcon;
        Random gerador = new Random();
        while(numeros.size() < qNum) {
            int num = gerador.nextInt(qNum/2)+1;
            long quantia = numeros.stream().filter(x -> x == num).count();
            if(quantia < 2) numeros.add(num);
        }
        clone = new ArrayList<>(numeros);
        if(nivel == 1 || nivel == 2) jogo(0,100,1000,300);
        if(nivel == 3 || nivel == 4) jogo(0,50,1200,200);
        if(nivel == 5 || nivel == 6) jogo(0,30,1200,160);
        if(nivel == 7 || nivel == 8) jogo(50,30,1200,160);
    }
    
    private void definirJogo(){
        
        if(frame.getNivel() == 1 || frame.getNivel() == 3) {
            
            if(frame.getErros() == 10) derrota();           
        } 
        
        if(frame.getNivel() == 2 || frame.getNivel() == 4) {
            
            if(frame.getErros() == 5) derrota();            
        } 
        
        if(frame.getNivel() == 5 || frame.getNivel() == 7) {
            
            if(frame.getErros() == 20) derrota();            
        } 
        
         if(frame.getNivel() == 6 || frame.getNivel() == 8) {
            
            if(frame.getErros() == 10) derrota();            
        }               
        
        if(clone.size() == 0) {
         
            frame.setMensagem("Parabéns! Você venceu!");
            frame.gameOver();
        }
    }
    
    private void derrota() {
        frame.setMensagem("Fim de jogo! Você perdeu!");
        frame.gameOver();
    }
       
}
