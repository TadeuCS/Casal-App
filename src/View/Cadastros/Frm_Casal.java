/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Cadastros;

import Util.IntegerDocument;
import Util.Manager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Tadeu
 */
public class Frm_Casal extends javax.swing.JFrame {

    DefaultTableModel model;
    Manager manager = new Manager();
    ResultSet rs;
    String diretorio = null;

    public Frm_Casal() {
        initComponents();
        tbFoto.setVisible(false);
        txt_numero.setDocument(new IntegerDocument(5));
        camposOFF();
        txt_codigo.setEnabled(false);
        txt_operacao.setEnabled(false);
        count();
        model = (DefaultTableModel) tabela1.getModel();
        listaPessoas();
    }

    public void count() {
        txt_qtde.setText(tabela1.getRowCount() + "");
    }

    public void filtrar() {
        TableRowSorter sorter = new TableRowSorter(model);
        tabela1.setRowSorter(sorter);
        String texto = txt_filtro.getText();
        try {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Valor Não Encontrado!!!", "AVISO - Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void camposOFF() {
        txt_codigo.setEnabled(false);
        txt_casal.setEnabled(false);
        txt_telefone.setEnabled(false);
        txt_endereco.setEnabled(false);
        txt_numero.setEnabled(false);
        txt_complemento.setEnabled(false);
        txt_cidade.setEnabled(false);
        txt_cep.setEnabled(false);
        txt_bairro.setEnabled(false);
        botoesON();
    }

    public void camposON() {
        txt_casal.setEnabled(true);
        txt_telefone.setEnabled(true);
        txt_endereco.setEnabled(true);
        txt_cidade.setEnabled(true);
        txt_cep.setEnabled(true);
        txt_numero.setEnabled(true);
        txt_complemento.setEnabled(true);
        txt_bairro.setEnabled(true);
        txt_casal.requestFocus();
    }

    public void botoesON() {
        btn_incluir.setEnabled(true);
        btn_editar.setEnabled(true);
        btn_apagar.setEnabled(true);
        btn_cancelar.setEnabled(false);
        btn_salvar.setEnabled(false);
    }

    public void botoesOFF() {
        btn_incluir.setEnabled(false);
        btn_editar.setEnabled(false);
        btn_apagar.setEnabled(false);
        btn_cancelar.setEnabled(true);
        btn_salvar.setEnabled(true);
    }

    public void limpaCampos() {
        txt_casal.setText(null);
        txt_bairro.setText(null);
        txt_cep.setText(null);
        txt_cidade.setText(null);
        txt_codigo.setText(null);
        txt_complemento.setText(null);
        txt_endereco.setText(null);
        txt_numero.setText(null);
        txt_telefone.setText(null);
    }

    public void limpaTabela() {
        try {
            while (model.getRowCount() > 0) {
                for (int i = 1; i <= model.getRowCount(); i++) {
                    model.removeRow(0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void listaPessoas() {
        limpaTabela();
        rs = manager.consulta("select * from pessoa ORDER BY CODPESSOA");
        try {
            while (rs.next()) {
                String[] linha = new String[]{rs.getString("CODPESSOA"), rs.getString("CASAL"), rs.getString("telefone"),
                    rs.getString("cep"), rs.getString("complemento"), rs.getString("numero")};
                model.addRow(linha);
            }
            count();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "erro" + e.getMessage());
        }
    }

    public int getCodCasal() {
        int codcasal = 0;
        try {
            rs = manager.consulta("SELECT MAX(CODPESSOA) QTDE FROM PESSOA");
            while (rs.next()) {
                codcasal = Integer.parseInt(rs.getString("qtde")) + 1;
            }
        } catch (Exception e) {

        }
        return codcasal;
    }

    public void Salvar(String nome, String telefone, String cep, int numero, String complemento, String logradouro, String bairro, String cidade) {
        if (txt_operacao.getText().compareTo("Inclusão") == 0) {
            try {
                int codigo= getCodCasal();
                manager.insere("INSERT INTO PESSOA (CODPESSOA,CASAL,TELEFONE,CEP,NUMERO,COMPLEMENTO,LOGRADOURO,BAIRRO,CIDADE) VALUES ("
                        + codigo + ",'" + nome + "','" + telefone + "','" + cep + "'," + numero + ",'" + complemento + "','" + logradouro + "','" + bairro + "','" + cidade + "');");
                upaImagemByDiretorio(diretorio, codigo + "");
                JOptionPane.showMessageDialog(null, "Casal Inserido com Sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro.\n Casal não foi inserido no banco de dados!");
            }
            listaPessoas();
            limpaCampos();
        }
        if (txt_operacao.getText().compareTo("Alteração") == 0) {
            try {
                manager.insere("UPDATE PESSOA SET"
                        + " CASAL='" + nome + "',"
                        + "TELEFONE='" + telefone + "',"
                        + "CEP='" + cep + "',"
                        + "NUMERO=" + numero + ","
                        + "COMPLEMENTO='" + complemento + "',"
                        + "LOGRADOURO='" + logradouro + "',"
                        + "BAIRRO='" + bairro + "',"
                        + "CIDADE='" + cidade + "'"
                        + " WHERE CODPESSOA=" + txt_codigo.getText() + ";"
                );
                upaImagemByDiretorio(diretorio, txt_codigo.getText() + "");
                JOptionPane.showMessageDialog(null, "Casal Alterado com Sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro.\n Casal não foi alterado no banco de dados!");
            }
            listaPessoas();
            limpaCampos();
            
        }
    }

    public void validaCEP(String cep) {
        try {
            rs = manager.consulta("select * from endereco where cep='" + cep + "'");
            if (rs.next()) {
                txt_endereco.setText(rs.getString("LOGRADOURO"));
                txt_bairro.setText(rs.getString("BAIRRO"));
                txt_cidade.setText(rs.getString("CIDADE"));
                txt_numero.requestFocus();
            } else {
                Frm_CEP f = new Frm_CEP();
                f.setVisible(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CEP não encontrado!");
        }
    }

    public void validaCampos() {
        if (txt_casal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome do Casal Inválido!");
            txt_casal.requestFocus();
        } else {
            if (txt_telefone.getText().compareTo("(  )     -    ") == 0) {
                JOptionPane.showMessageDialog(null, "Telefone Inválido!");
                txt_telefone.requestFocus();
            } else {
                if (txt_cep.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "CEP Inválido.\n Informe um CEP válido e pressione ENTER em seu campo!");
                    txt_cep.requestFocus();
                } else {
                    if (txt_endereco.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Endereço inválido!");
                        txt_endereco.requestFocus();
                    } else {
                        if (txt_bairro.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Bairro inválido!");
                            txt_bairro.requestFocus();
                        } else {
                            if (txt_numero.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Número Inválido");
                                txt_numero.requestFocus();
                            } else {
                                if (txt_cidade.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Cidade inválida!");
                                    txt_cidade.requestFocus();
                                } else {
//                                    if (diretorio==null) {
//                                        buscaImagem();
//                                    } else {
                                        Salvar(txt_casal.getText().toUpperCase(), txt_telefone.getText(), txt_cep.getText().replaceAll("-", ""),
                                                Integer.parseInt(txt_numero.getText()), txt_complemento.getText().toUpperCase(),
                                                txt_endereco.getText().toUpperCase(), txt_bairro.getText().toUpperCase(), txt_cidade.getText().toUpperCase());
//                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public void buscaPessoa(String codpessoa) {
        try {
            rs = manager.consulta("SELECT * FROM Pessoa P WHERE P.CODPESSOA =" + codpessoa + ";");
            while (rs.next()) {
                txt_casal.setText(rs.getString("casal"));
                txt_telefone.setText(rs.getString("telefone"));
                txt_cep.setText(rs.getString("CEP"));
                txt_endereco.setText(rs.getString("logradouro"));
                txt_bairro.setText(rs.getString("bairro"));
                txt_cidade.setText(rs.getString("cidade"));
                txt_complemento.setText(rs.getString("complemento"));
                txt_numero.setText(rs.getString("numero"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar a PESSOA.\n" + ex.getMessage());
        }
    }

    public void consultar() {
        txt_codigo.setEnabled(true);
        txt_codigo.requestFocus();

        botoesOFF();
        btn_salvar.setEnabled(false);
        btn_editar.setEnabled(true);
        btn_apagar.setEnabled(true);
    }

    public void apagar() {
        if (tabela1.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "Selecione pelo menos uma Linha da Tabela!");
        } else {
            if (tabela1.getSelectedRowCount() > 1) {
                JOptionPane.showMessageDialog(null, "Selecione apenas uma Linha da Tabela!");
            } else {
                if (tabela1.getSelectedRowCount() == 1) {
                    if (JOptionPane.showConfirmDialog(null, "Deseja apagar o casal: " + tabela1.getValueAt(tabela1.getSelectedRow(), 1).toString(), "Atenção", 0, JOptionPane.YES_OPTION) == 0) {
                        try {
                            manager.insere("DELETE FROM PESSOA WHERE CODPESSOA=" + tabela1.getValueAt(tabela1.getSelectedRow(), 0).toString());
                            model.removeRow(tabela1.getSelectedRow());
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Erro.\n Casal não foi apagado do banco de dados!\n Verifique se este Casal tem algum Historico cadastrado");
                        }
                    }
                    count();
                }
            }

        }
        tabela1.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_fundo = new javax.swing.JPanel();
        pnl_dados = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_casal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_telefone = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_endereco = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_cep = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_bairro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_cidade = new javax.swing.JTextField();
        txt_codigo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_numero = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_complemento = new javax.swing.JTextField();
        pnl_botoes = new javax.swing.JPanel();
        btn_incluir = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_apagar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salvar = new javax.swing.JButton();
        txt_operacao = new javax.swing.JTextField();
        pnl_fundo1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_filtro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txt_qtde = new javax.swing.JLabel();
        tbFoto = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Casais");
        setResizable(false);

        pnl_fundo.setBackground(new java.awt.Color(45, 113, 97));

        pnl_dados.setBackground(new java.awt.Color(45, 113, 97));
        pnl_dados.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("Casal *:");

        jLabel3.setText("Telefone *:");

        try {
            txt_telefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_telefone.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setText("Endereço *:");

        txt_endereco.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_enderecoFocusGained(evt);
            }
        });

        jLabel5.setText("CEP *:");

        try {
            txt_cep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_cep.setText("");
        txt_cep.setToolTipText("");
        txt_cep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_cepKeyPressed(evt);
            }
        });

        jLabel6.setText("Bairro *:");

        jLabel7.setText("Cidade *:");

        txt_codigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setText("Código:");

        jLabel12.setText("Número *:");

        jLabel13.setText("Complemento :");

        javax.swing.GroupLayout pnl_dadosLayout = new javax.swing.GroupLayout(pnl_dados);
        pnl_dados.setLayout(pnl_dadosLayout);
        pnl_dadosLayout.setHorizontalGroup(
            pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnl_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_casal))
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_cep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_complemento))
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(7, 7, 7)
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_dadosLayout.createSequentialGroup()
                                .addComponent(txt_bairro, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_cidade))
                            .addComponent(txt_endereco))))
                .addContainerGap())
        );
        pnl_dadosLayout.setVerticalGroup(
            pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txt_casal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txt_cep, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txt_complemento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_bairro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txt_cidade, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_botoes.setBackground(new java.awt.Color(45, 113, 97));
        pnl_botoes.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btn_incluir.setBackground(new java.awt.Color(45, 113, 97));
        btn_incluir.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btn_incluir.setText("Incluir");
        btn_incluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_incluirActionPerformed(evt);
            }
        });

        btn_editar.setBackground(new java.awt.Color(45, 113, 97));
        btn_editar.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btn_editar.setText("Editar");
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        btn_apagar.setBackground(new java.awt.Color(45, 113, 97));
        btn_apagar.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btn_apagar.setText("Apagar");
        btn_apagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_apagarActionPerformed(evt);
            }
        });

        btn_cancelar.setBackground(new java.awt.Color(45, 113, 97));
        btn_cancelar.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        btn_salvar.setBackground(new java.awt.Color(45, 113, 97));
        btn_salvar.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btn_salvar.setText("Salvar");
        btn_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salvarActionPerformed(evt);
            }
        });

        txt_operacao.setEditable(false);
        txt_operacao.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout pnl_botoesLayout = new javax.swing.GroupLayout(pnl_botoes);
        pnl_botoes.setLayout(pnl_botoesLayout);
        pnl_botoesLayout.setHorizontalGroup(
            pnl_botoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_botoesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btn_incluir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_apagar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_operacao, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnl_botoesLayout.setVerticalGroup(
            pnl_botoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_botoesLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnl_botoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_incluir)
                    .addComponent(btn_editar)
                    .addComponent(btn_apagar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salvar)
                    .addComponent(txt_operacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        pnl_fundo1.setBackground(new java.awt.Color(45, 113, 97));
        pnl_fundo1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setText("Filtro:");

        txt_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_filtroKeyReleased(evt);
            }
        });

        tabela1.setBackground(new java.awt.Color(102, 102, 102));
        tabela1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Casal", "Telefone", "CEP", "Complemento", "Número"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabela1.getTableHeader().setReorderingAllowed(false);
        tabela1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabela1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabela1);
        if (tabela1.getColumnModel().getColumnCount() > 0) {
            tabela1.getColumnModel().getColumn(0).setMinWidth(60);
            tabela1.getColumnModel().getColumn(0).setPreferredWidth(60);
            tabela1.getColumnModel().getColumn(0).setMaxWidth(60);
            tabela1.getColumnModel().getColumn(2).setMinWidth(100);
            tabela1.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabela1.getColumnModel().getColumn(2).setMaxWidth(100);
            tabela1.getColumnModel().getColumn(3).setMinWidth(80);
            tabela1.getColumnModel().getColumn(3).setPreferredWidth(80);
            tabela1.getColumnModel().getColumn(3).setMaxWidth(80);
            tabela1.getColumnModel().getColumn(5).setMinWidth(60);
            tabela1.getColumnModel().getColumn(5).setPreferredWidth(60);
            tabela1.getColumnModel().getColumn(5).setMaxWidth(60);
        }

        jLabel9.setText("Quantidade:");

        txt_qtde.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txt_qtde.setForeground(new java.awt.Color(255, 255, 255));
        txt_qtde.setText("1234");

        javax.swing.GroupLayout pnl_fundo1Layout = new javax.swing.GroupLayout(pnl_fundo1);
        pnl_fundo1.setLayout(pnl_fundo1Layout);
        pnl_fundo1Layout.setHorizontalGroup(
            pnl_fundo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_fundo1Layout.createSequentialGroup()
                .addGroup(pnl_fundo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnl_fundo1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(pnl_fundo1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_qtde)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnl_fundo1Layout.setVerticalGroup(
            pnl_fundo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_fundo1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_fundo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_qtde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbFoto.setBackground(new java.awt.Color(45, 113, 97));
        tbFoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Foto"));

        javax.swing.GroupLayout pnl_fundoLayout = new javax.swing.GroupLayout(pnl_fundo);
        pnl_fundo.setLayout(pnl_fundoLayout);
        pnl_fundoLayout.setHorizontalGroup(
            pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_fundoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_fundoLayout.createSequentialGroup()
                        .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnl_botoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl_fundo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(pnl_fundoLayout.createSequentialGroup()
                        .addComponent(pnl_dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tbFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        pnl_fundoLayout.setVerticalGroup(
            pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_fundoLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tbFoto)
                    .addComponent(pnl_dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_botoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnl_fundo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
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

    private void btn_incluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_incluirActionPerformed
        camposON();
        botoesOFF();
        txt_operacao.setText("Inclusão");
    }//GEN-LAST:event_btn_incluirActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        camposOFF();
        txt_operacao.setText(null);
        tabela1.setEnabled(true);
        limpaCampos();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarActionPerformed
        validaCampos();
    }//GEN-LAST:event_btn_salvarActionPerformed

    private void txt_enderecoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_enderecoFocusGained

    }//GEN-LAST:event_txt_enderecoFocusGained

    private void txt_cepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cepKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String cep = txt_cep.getText().replaceAll("-", "").trim();
            if (cep.equals("0")) {
                txt_cep.setText("38700-000");
                txt_cidade.setText("PATOS DE MINAS");
                txt_numero.requestFocus();
            } else {
                validaCEP(txt_cep.getText().replaceAll("-", ""));
            }
        }
    }//GEN-LAST:event_txt_cepKeyPressed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        if (tabela1.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "Selecione pelo menos uma Linha da Tabela!");
        } else {
            if (tabela1.getSelectedRowCount() > 1) {
                JOptionPane.showMessageDialog(null, "Selecione apenas uma Linha da Tabela!");
            } else {
                if (tabela1.getSelectedRowCount() == 1) {
                    int codigo = Integer.parseInt(tabela1.getValueAt(tabela1.getSelectedRow(), 0).toString());
                    txt_codigo.setText(codigo + "");
                    buscaPessoa(codigo + "");
                    tabela1.setEnabled(false);
                    camposON();
                    botoesOFF();
                    txt_operacao.setText("Alteração");
                }
            }
        }
    }//GEN-LAST:event_btn_editarActionPerformed

    private void txt_filtroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filtroKeyReleased
        filtrar();
    }//GEN-LAST:event_txt_filtroKeyReleased

    private void btn_apagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_apagarActionPerformed
        tabela1.setEnabled(false);
        apagar();
    }//GEN-LAST:event_btn_apagarActionPerformed

    private void tabela1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela1MousePressed
//        carregaImagem(this.getClass().getResource(tabela1.getValueAt(tabela1.getSelectedRow(), 0).toString()));
    }//GEN-LAST:event_tabela1MousePressed

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
            java.util.logging.Logger.getLogger(Frm_Casal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm_Casal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm_Casal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm_Casal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_Casal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_apagar;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_incluir;
    private javax.swing.JButton btn_salvar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnl_botoes;
    private javax.swing.JPanel pnl_dados;
    private javax.swing.JPanel pnl_fundo;
    private javax.swing.JPanel pnl_fundo1;
    private javax.swing.JTable tabela1;
    private javax.swing.JScrollPane tbFoto;
    private javax.swing.JTextField txt_bairro;
    private javax.swing.JTextField txt_casal;
    private javax.swing.JFormattedTextField txt_cep;
    private javax.swing.JTextField txt_cidade;
    private javax.swing.JTextField txt_codigo;
    private javax.swing.JTextField txt_complemento;
    private javax.swing.JTextField txt_endereco;
    private javax.swing.JTextField txt_filtro;
    private javax.swing.JTextField txt_numero;
    private javax.swing.JTextField txt_operacao;
    private javax.swing.JLabel txt_qtde;
    private javax.swing.JFormattedTextField txt_telefone;
    // End of variables declaration//GEN-END:variables

    private void buscaImagem() {
        JFileChooser c = new JFileChooser();
        c.showOpenDialog(this);//abre o arquivo  
        File file = c.getSelectedFile();//abre o arquivo selecionado  
        try {
            Path path = Paths.get(file.getAbsolutePath());
            diretorio = path.toString();
            if (diretorio.endsWith("png") || diretorio.endsWith("jpg")) {
                carregaImagem(diretorio);
            } else {
                JOptionPane.showMessageDialog(this, "Extenção do arquivo selecionado inválido!\n "
                        + "Tente imagens com extenção: PNG ou JPG");
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(this, "Não carregou nenhuma Imagem");
        }
    }

    private void upaImagemByDiretorio(String caminho, String nome) {
        FileInputStream origem;
        FileOutputStream destino;

        FileChannel fcOrigem;
        FileChannel fcDestino;

        try {
            origem = new FileInputStream(caminho);//arquivo que você quer copiar  
            File file = new File("Frm_principal.java");
            String caminhoDestino = file.getAbsolutePath().replaceAll(file.getPath(), "").replace("\\", "/");
            destino = new FileOutputStream(caminhoDestino + "src/Fotos/" + nome + ".png");//onde irá ficar a copia do aquivo (ide)
//            destino = new FileOutputStream(caminhoDestino + "Fotos/" + nome + ".png");//onde irá ficar a copia do aquivo  (compilado)

            fcOrigem = origem.getChannel();
            fcDestino = destino.getChannel();
            fcOrigem.transferTo(0, fcOrigem.size(), fcDestino);//copiando o arquivo e salvando no diretório que você escolheu  
            origem.close();
            destino.close();
//            foto.setIcon(null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private ImageIcon getImagemByCaminho(String caminho) {
        ImageIcon imagem = new ImageIcon(caminho);
        if (imagem != null) {
            return imagem;
        } else {
            System.err.println("Não foi possível encontrar o arquivo: " + caminho);
            return null;
        }
    }

    private void carregaImagem(String caminho) {
        try {
//            foto.setIcon(new ImageIcon(alteraTamanhoImagem(getImagemByCaminho(caminho.replace("\\", "/")).getImage(), 120)));
//            foto.revalidate();
//            foto.repaint();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Image alteraTamanhoImagem(Image img, int largura) {
        //Calcula a proporção para descobrir a nova altura  
        double proportion = largura / (double) img.getWidth(null);
        int newHeight = (int) (img.getHeight(null) * proportion);

        //Cria a imagem de destino          
        BufferedImage newImage = new BufferedImage(largura, newHeight, BufferedImage.TYPE_INT_ARGB);

        //Desenha sobre ela usando filtro Bilinear (o java não possui trinilear ou anisotrópica).  
        Graphics2D g2d = newImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, largura, newHeight, null);
        g2d.dispose();

        return newImage;
    }
}
