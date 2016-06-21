/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Cadastros;

import Util.Criptografia;
import Util.Manager;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Tadeu
 */
public class Frm_Usuario extends javax.swing.JFrame {

    ResultSet rs;
    Manager manager = new Manager();

    public Frm_Usuario() {
        initComponents();
        icone1.setVisible(false);
        icone2.setVisible(false);
        botoesON();
        campoOFF();
    }

    public void campoOFF() {
        txt_usuario.setEnabled(false);
        txt_senha.setEnabled(false);
        txt_confirmaSenha.setEnabled(false);
        limpaCampos();
    }

    public void campoON() {
        txt_usuario.setEnabled(true);
        txt_senha.setEnabled(true);
        txt_confirmaSenha.setEnabled(true);
        txt_usuario.requestFocus();
    }

    public void botoesOFF() {
        btn_inclusao.setEnabled(false);
        btn_bloquear.setEnabled(false);
        btn_salvar.setEnabled(true);
        btn_cancelar.setEnabled(true);
    }

    public void botoesON() {
        btn_inclusao.setEnabled(true);
        btn_bloquear.setEnabled(true);
        btn_salvar.setEnabled(false);
        btn_cancelar.setEnabled(false);
    }

    public boolean validaUsuario(String usuario) {
        boolean resultado = false;
        try {
            rs = manager.consulta("SELECT count(*) qtde FROM USUARIO WHERE USUARIO LIKE '" + usuario + "';");
            while (rs.next()) {
                if (Integer.parseInt(rs.getString("qtde")) == 1) {
                    resultado = false;
                } else {
                    resultado = true;
                }
            }
        } catch (Exception e) {
        }
        return resultado;
    }

    public void limpaCampos() {
        txt_usuario.setText(null);
        txt_senha.setText(null);
        txt_confirmaSenha.setText(null);
    }

    public boolean validaSenha(String senha) {
        boolean resultado = false;
        if (txt_senha.getText().equals(txt_confirmaSenha.getText()) != true) {
            resultado = false;
        } else {
            resultado = true;
        }
        return resultado;
    }

    public int getCodUsuario() {
        int CodUsuario=0;
        try {
            rs = manager.consulta("SELECT COUNT(*) QTDE FROM USUARIO");
            while(rs.next()){
             CodUsuario=Integer.parseInt(rs.getString("qtde"))+1;
            }
        } catch (Exception e) {
        
        }
        return CodUsuario;
    }

    public void salvar(String usuario, String senha) {
        if (txt_senha.getText().equals(txt_confirmaSenha.getText()) == true) {
            try{
            manager.insere("INSERT INTO USUARIO(CODUSUARIO,USUARIO,SENHA) VALUES ("+getCodUsuario()+",'" + usuario + "','" + Criptografia.criptografar(senha) + "');");
            JOptionPane.showMessageDialog(null, "Usuário salvo com sucesso!");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Erro.\n Usuário não foi inserido no banco de dados!");
            }
            campoOFF();
            botoesON();
        } else {
            txt_senha.setText(null);
            txt_confirmaSenha.setText(null);
            txt_senha.requestFocus();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_fundo = new javax.swing.JPanel();
        pnl_dados = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_usuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lb_iconeUsuario = new javax.swing.JLabel();
        lb_iconeSenha = new javax.swing.JLabel();
        txt_senha = new javax.swing.JPasswordField();
        txt_confirmaSenha = new javax.swing.JPasswordField();
        icone2 = new javax.swing.JLabel();
        icone1 = new javax.swing.JLabel();
        btn_inclusao = new javax.swing.JButton();
        btn_bloquear = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salvar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Usuario");
        setResizable(false);

        pnl_fundo.setBackground(new java.awt.Color(45, 113, 97));

        pnl_dados.setBackground(new java.awt.Color(45, 113, 97));
        pnl_dados.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Usuário *:");

        txt_usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_usuarioKeyPressed(evt);
            }
        });

        jLabel2.setText("Senha *:");

        jLabel3.setText("Confirma Senha *:");

        txt_senha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_senhaKeyPressed(evt);
            }
        });

        txt_confirmaSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_confirmaSenhaKeyPressed(evt);
            }
        });

        icone2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/invalido.png"))); // NOI18N

        icone1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/valido.png"))); // NOI18N

        javax.swing.GroupLayout pnl_dadosLayout = new javax.swing.GroupLayout(pnl_dados);
        pnl_dados.setLayout(pnl_dadosLayout);
        pnl_dadosLayout.setHorizontalGroup(
            pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createSequentialGroup()
                        .addComponent(icone2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createSequentialGroup()
                        .addComponent(icone1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_confirmaSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_senha, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                        .addComponent(txt_usuario)))
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lb_iconeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lb_iconeSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_dadosLayout.setVerticalGroup(
            pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_iconeUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(icone1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txt_senha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(icone2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txt_confirmaSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(lb_iconeSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );

        btn_inclusao.setBackground(new java.awt.Color(45, 113, 97));
        btn_inclusao.setText("Inclusão");
        btn_inclusao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_inclusaoActionPerformed(evt);
            }
        });

        btn_bloquear.setBackground(new java.awt.Color(45, 113, 97));
        btn_bloquear.setText("Bloquear");
        btn_bloquear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bloquearActionPerformed(evt);
            }
        });

        btn_cancelar.setBackground(new java.awt.Color(45, 113, 97));
        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        btn_salvar.setBackground(new java.awt.Color(45, 113, 97));
        btn_salvar.setText("Salvar");
        btn_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salvarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_fundoLayout = new javax.swing.GroupLayout(pnl_fundo);
        pnl_fundo.setLayout(pnl_fundoLayout);
        pnl_fundoLayout.setHorizontalGroup(
            pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_fundoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_fundoLayout.createSequentialGroup()
                        .addComponent(btn_inclusao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_bloquear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnl_fundoLayout.setVerticalGroup(
            pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_fundoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_dados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_inclusao)
                    .addComponent(btn_bloquear)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salvar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_fundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_fundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        botoesON();
        campoOFF();
        setTitle("Cadastro de Usuario");
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void txt_usuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_usuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (validaUsuario(txt_usuario.getText().toUpperCase()) == true) {
                lb_iconeUsuario.setIcon(icone1.getIcon());
                txt_senha.requestFocus();
            } else {
                lb_iconeUsuario.setIcon(icone2.getIcon());
            }
        }
    }//GEN-LAST:event_txt_usuarioKeyPressed

    private void txt_senhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_senhaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt_confirmaSenha.requestFocus();
        }

    }//GEN-LAST:event_txt_senhaKeyPressed

    private void txt_confirmaSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_confirmaSenhaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (validaSenha(txt_senha.getText()) == true) {
                lb_iconeSenha.setIcon(icone1.getIcon());
                btn_salvar.requestFocus();
            } else {
                lb_iconeSenha.setIcon(icone2.getIcon());
                txt_senha.setText(null);
                txt_confirmaSenha.setText(null);
                txt_senha.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_confirmaSenhaKeyPressed

    private void btn_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarActionPerformed
        salvar(txt_usuario.getText().toUpperCase(), txt_senha.getText());
    }//GEN-LAST:event_btn_salvarActionPerformed

    private void btn_inclusaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inclusaoActionPerformed
        botoesOFF();
        campoON();
    }//GEN-LAST:event_btn_inclusaoActionPerformed

    private void btn_bloquearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bloquearActionPerformed
        String usuario = JOptionPane.showInputDialog("Usuário que será BLOQUEADO: ").toUpperCase();
        try {
            rs = manager.consulta("SELECT * FROM USUARIO WHERE USUARIO LIKE '" + usuario + "';");
            while (rs.next()) {
                if (usuario.equals(rs.getString("usuario")) == true) {
                    manager.insere("UPDATE USUARIO SET ATIVO='N' WHERE USUARIO LIKE '" + usuario + "';");
                    JOptionPane.showMessageDialog(null, "Usuário: " + usuario + " bloqueado!");
                }
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_btn_bloquearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frm_Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm_Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm_Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm_Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_Usuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_bloquear;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_inclusao;
    private javax.swing.JButton btn_salvar;
    private javax.swing.JLabel icone1;
    private javax.swing.JLabel icone2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lb_iconeSenha;
    private javax.swing.JLabel lb_iconeUsuario;
    private javax.swing.JPanel pnl_dados;
    private javax.swing.JPanel pnl_fundo;
    private javax.swing.JPasswordField txt_confirmaSenha;
    private javax.swing.JPasswordField txt_senha;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
