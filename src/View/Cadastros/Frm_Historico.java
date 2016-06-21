/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Cadastros;

import Util.IntegerDocument;
import Util.Manager;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Tadeu
 */
public class Frm_Historico extends javax.swing.JFrame {

    ResultSet rs;
    DefaultTableModel model;
    Manager manager = new Manager();

    public Frm_Historico() {

        initComponents();
        txt_encontro.setDocument(new IntegerDocument(4));
        model = (DefaultTableModel) tb_pessoas.getModel();
        carregaFuncoes();
        carregaParoquias();
        botoesON();
        camposOFF();
        listar();
        tb_pessoas.setEnabled(true);
    }

    public void camposON() {
        txt_ano.setEditable(true);
        txt_encontro.setEditable(true);
        txt_codigo.setEditable(true);
        txt_codigo.requestFocus();
    }

    public void camposOFF() {
        txt_ano.setEditable(false);
        txt_encontro.setEditable(false);
        txt_codigo.setEditable(false);
        limpaCampos();
    }

    public void botoesOFF() {
        btn_incluir.setEnabled(false);
        btn_editar.setEnabled(false);
        btn_apagar.setEnabled(false);
        btn_salvar.setEnabled(true);
        btn_cancelar.setEnabled(true);
    }

    public void botoesON() {
        btn_incluir.setEnabled(true);
        btn_editar.setEnabled(true);
        btn_apagar.setEnabled(true);
        btn_salvar.setEnabled(false);
        btn_cancelar.setEnabled(false);
    }

    public void carregaFuncoes() {
        cbx_funcao.removeAllItems();
        try {
            rs = manager.consulta("SELECT * FROM FUNCAO");
            while (rs.next()) {
                cbx_funcao.addItem(rs.getString("descricao"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Listar as Funções.\n" + ex.getMessage());
        }
    }

    public void carregaParoquias() {
        cbx_paroquia.removeAllItems();
        try {
            rs = manager.consulta("SELECT * FROM PAROQUIA");
            while (rs.next()) {
                cbx_paroquia.addItem(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Listar as Paroquias.\n" + ex.getMessage());
        }
    }

    public void count() {
        txt_qtde.setText(tb_pessoas.getRowCount() + "");
    }

    public void listar() {
        limpaTabela();
        ResultSet rs;
        try {
            rs = manager.consulta("SELECT * FROM HISTORICO H INNER JOIN FUNCAO F ON H.CODFUNCAO=F.CODFUNCAO INNER JOIN PESSOA P ON P.CODPESSOA=H.CODPESSOA INNER JOIN PAROQUIA R ON H.CODPAROQUIA = R.CODPAROQUIA ORDER BY P.CODPESSOA");
            while (rs.next()) {
                String[] linha = new String[]{rs.getString("CODPESSOA"), rs.getString("CASAL"), rs.getString("TELEFONE"), rs.getString("DESCRICAO"),
                    rs.getString("NOME"), rs.getString("ANO"), rs.getString("ENCONTRO")};
                model.addRow(linha);
            }
            count();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Listar Casais.\n" + ex.getMessage());
        }

    }

    public void limpaTabela() {
        try {
            DefaultTableModel tblRemove = (DefaultTableModel) tb_pessoas.getModel();
            while (tblRemove.getRowCount() > 0) {
                for (int i = 1; i <= tblRemove.getRowCount(); i++) {
                    tblRemove.removeRow(0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public void limpaCampos() {
        txt_encontro.setText(null);
        txt_codigo.setText(null);
        txt_descricao.setText(null);
        txt_ano.setText(null);
    }

    public int getCodHistorico() {
        int codhistorico = 0;
        try {
            rs = manager.consulta("SELECT max(CODHISTORICO) QTDE FROM HISTORICO");
            while (rs.next()) {
                codhistorico = Integer.parseInt(rs.getString("qtde")) + 1;
            }
        } catch (Exception e) {

        }
        return codhistorico;
    }

    public void insereHistorico(int ano, int codpessoa, int codfuncao, int codparoquia, int encontro) {
        ResultSet rs;
        try {
            rs = manager.consulta("SELECT count(*) qtde FROM HISTORICO WHERE CODPESSOA =" + codpessoa + " AND ANO LIKE '" + ano + "' and codfuncao=" + codfuncao + " and codparoquia=" + codparoquia);
            while (rs.next()) {
                if (Integer.parseInt(rs.getString("qtde")) == 0) {
                    if (txt_operacao.getText().equals("Inclusão") == true) {
                        manager.insere("INSERT INTO HISTORICO (CODHISTORICO,ANO,CODPESSOA,CODFUNCAO,CODPAROQUIA,ENCONTRO) VALUES ("
                                + getCodHistorico() + "," + ano + "," + codpessoa + "," + codfuncao + "," + codparoquia + "," + encontro + ");"
                        );
                        limpaCampos();
                        listar();
                        JOptionPane.showMessageDialog(null, "Historico inserido com sucesso!");
                    }
                    if (txt_operacao.getText().equals("Alteração") == true) {
                        manager.insere("UPDATE HISTORICO SET ANO=" + ano + ", CODFUNCAO=" + codfuncao + ",CODPESSOA=" + codpessoa + ", CODPAROQUIA=" + codparoquia + ", ENCONTRO=" + encontro
                                + " WHERE CODPESSOA=" + tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 0)
                                + "  AND ANO LIKE '" + tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 5)
                                + "' AND CODFUNCAO=" + buscaCodFuncao(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 3).toString()) + ";");
                        limpaCampos();
                        listar();
                        JOptionPane.showMessageDialog(null, "Historico alterado com sucesso!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Casal já tem uma função neste ano!");
                }
            }

        } catch (Exception e) {
        }
    }

    public int buscaCodParoquia(String descricao) {
        int resultado = 0;
        try {
            rs = manager.consulta("SELECT * FROM PAROQUIA WHERE nome LIKE '" + descricao + "';");
            while (rs.next()) {
                resultado = Integer.parseInt(rs.getString("codparoquia"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar a Paroquias.\n" + ex.getMessage());
        }
        return resultado;
    }

    public int buscaPessoa(String codigo) {
        int resultado = 0;
        try {
            rs = manager.consulta("SELECT * FROM Pessoa WHERE codpessoa LIKE '" + codigo + "';");
            while (rs.next()) {
                txt_descricao.setText(rs.getString("casal"));
                txt_ano.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar a PESSOA.\n" + ex.getMessage());
        }
        return resultado;
    }

    public int buscaCodFuncao(String descricao) {
        int resultado = 0;
        try {
            rs = manager.consulta("SELECT * FROM funcao WHERE descricao LIKE '" + descricao + "';");
            while (rs.next()) {
                resultado = Integer.parseInt(rs.getString("codfuncao"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar a FUNÇÃO.\n" + ex.getMessage());
        }
        return resultado;
    }

    public void filtrar() {
        TableRowSorter sorter = new TableRowSorter(tb_pessoas.getModel());
        tb_pessoas.setRowSorter(sorter);
        String texto = txt_filtro.getText();
        try {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Valor Não Encontrado!!!", "AVISO - Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void validaCampos() {
        if (txt_codigo.getText().compareTo("") == 0) {
            JOptionPane.showMessageDialog(null, "Casal inválido!");
            txt_codigo.requestFocus();
        } else {
            if (txt_ano.getText().replaceAll(" ", "").compareTo("") == 0) {
                JOptionPane.showMessageDialog(null, "Ano Inválido");
                txt_ano.requestFocus();
            } else {
                insereHistorico(Integer.parseInt(txt_ano.getText()), Integer.parseInt(txt_codigo.getText()),
                        buscaCodFuncao(cbx_funcao.getSelectedItem().toString()),
                        buscaCodParoquia(cbx_paroquia.getSelectedItem().toString()), Integer.parseInt(txt_encontro.getText()));
            }
        }
    }

    public void apagar() {
        if (tb_pessoas.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "Selecione pelo menos uma Linha da Tabela!");
        } else {
            if (tb_pessoas.getSelectedRowCount() > 1) {
                JOptionPane.showMessageDialog(null, "Selecione apenas uma Linha da Tabela!");
            } else {
                if (tb_pessoas.getSelectedRowCount() == 1) {
                    if (JOptionPane.showConfirmDialog(null, "Deseja apagar o Historico de: " + tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 1).toString(), "", 0, JOptionPane.YES_OPTION) == 0) {
                        try {
                            manager.insere("DELETE FROM historico WHERE "
                                    + "     CODPESSOA=" + tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 0).toString()
                                    + " AND CODFUNCAO=" + buscaCodFuncao(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 3).toString())
                                    + " AND ANO LIKE '" + tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 5).toString() + "';");
                            model.removeRow(tb_pessoas.getSelectedRow());
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Erro.\n Historico não foi apagado no banco de dados!");
                        }
                    }
                    count();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnl_dados = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbx_funcao = new javax.swing.JComboBox();
        btn_novaFuncao = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        cbx_paroquia = new javax.swing.JComboBox();
        btn_novaParoquia = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_ano = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_codigo = new javax.swing.JTextField();
        txt_descricao = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_encontro = new javax.swing.JTextField();
        pnl_botoes = new javax.swing.JPanel();
        btn_incluir = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_apagar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salvar = new javax.swing.JButton();
        txt_operacao = new javax.swing.JTextField();
        pnl_fundo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_filtro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_pessoas = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txt_qtde = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lançamento de Historico");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 113, 97));

        pnl_dados.setBackground(new java.awt.Color(45, 113, 97));
        pnl_dados.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setText("Função:");

        cbx_funcao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbx_funcaoMouseClicked(evt);
            }
        });

        btn_novaFuncao.setBackground(new java.awt.Color(45, 113, 97));
        btn_novaFuncao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cadastro.png"))); // NOI18N
        btn_novaFuncao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_novaFuncaoActionPerformed(evt);
            }
        });

        jLabel14.setText("Paroquia:");

        cbx_paroquia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbx_paroquiaMouseClicked(evt);
            }
        });

        btn_novaParoquia.setBackground(new java.awt.Color(45, 113, 97));
        btn_novaParoquia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cadastro.png"))); // NOI18N
        btn_novaParoquia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_novaParoquiaActionPerformed(evt);
            }
        });

        jLabel1.setText("Ano *:");

        txt_ano.setEditable(false);
        try {
            txt_ano.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_ano.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel8.setText("Casal *:");

        jLabel2.setText("Código *:");

        txt_codigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_codigoKeyPressed(evt);
            }
        });

        txt_descricao.setEditable(false);

        jLabel5.setText("Encontro *:");

        txt_encontro.setEditable(false);

        javax.swing.GroupLayout pnl_dadosLayout = new javax.swing.GroupLayout(pnl_dados);
        pnl_dados.setLayout(pnl_dadosLayout);
        pnl_dadosLayout.setHorizontalGroup(
            pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbx_paroquia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnl_dadosLayout.createSequentialGroup()
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnl_dadosLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8))
                            .addGroup(pnl_dadosLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_ano, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_dadosLayout.createSequentialGroup()
                                .addComponent(txt_encontro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbx_funcao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txt_descricao))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_novaFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_novaParoquia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnl_dadosLayout.setVerticalGroup(
            pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8)
                    .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descricao, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_novaFuncao)
                    .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_ano, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel9)
                        .addComponent(cbx_funcao, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(txt_encontro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(cbx_paroquia, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_novaParoquia, javax.swing.GroupLayout.Alignment.TRAILING))
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
                .addGap(27, 27, 27)
                .addComponent(txt_operacao, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_botoesLayout.setVerticalGroup(
            pnl_botoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_botoesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnl_botoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_incluir)
                    .addComponent(btn_editar)
                    .addComponent(btn_apagar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salvar)
                    .addComponent(txt_operacao, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        pnl_fundo.setBackground(new java.awt.Color(45, 113, 97));
        pnl_fundo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("Filtro:");

        txt_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_filtroKeyReleased(evt);
            }
        });

        tb_pessoas.setBackground(new java.awt.Color(102, 102, 102));
        tb_pessoas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Casal", "Telefone", "Função", "Paróquia", "Ano", "Encontro"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_pessoas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tb_pessoas);
        if (tb_pessoas.getColumnModel().getColumnCount() > 0) {
            tb_pessoas.getColumnModel().getColumn(0).setMinWidth(70);
            tb_pessoas.getColumnModel().getColumn(0).setPreferredWidth(70);
            tb_pessoas.getColumnModel().getColumn(0).setMaxWidth(70);
            tb_pessoas.getColumnModel().getColumn(5).setMinWidth(70);
            tb_pessoas.getColumnModel().getColumn(5).setPreferredWidth(70);
            tb_pessoas.getColumnModel().getColumn(5).setMaxWidth(70);
            tb_pessoas.getColumnModel().getColumn(6).setMinWidth(70);
            tb_pessoas.getColumnModel().getColumn(6).setPreferredWidth(70);
            tb_pessoas.getColumnModel().getColumn(6).setMaxWidth(70);
        }

        jLabel4.setText("Quantidade:");

        txt_qtde.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txt_qtde.setForeground(new java.awt.Color(255, 255, 255));
        txt_qtde.setText("1234");

        javax.swing.GroupLayout pnl_fundoLayout = new javax.swing.GroupLayout(pnl_fundo);
        pnl_fundo.setLayout(pnl_fundoLayout);
        pnl_fundoLayout.setHorizontalGroup(
            pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_fundoLayout.createSequentialGroup()
                .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnl_fundoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(pnl_fundoLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_qtde)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnl_fundoLayout.setVerticalGroup(
            pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_fundoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_fundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_qtde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_botoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_fundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_dados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_botoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnl_fundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbx_funcaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbx_funcaoMouseClicked
        carregaFuncoes();
    }//GEN-LAST:event_cbx_funcaoMouseClicked

    private void btn_novaFuncaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_novaFuncaoActionPerformed
        Frm_Funcao cad = new Frm_Funcao();
        cad.setVisible(true);
    }//GEN-LAST:event_btn_novaFuncaoActionPerformed

    private void cbx_paroquiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbx_paroquiaMouseClicked
        carregaParoquias();
    }//GEN-LAST:event_cbx_paroquiaMouseClicked

    private void btn_novaParoquiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_novaParoquiaActionPerformed
        Frm_Paroquia p = new Frm_Paroquia();
        p.setVisible(true);
    }//GEN-LAST:event_btn_novaParoquiaActionPerformed

    private void btn_incluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_incluirActionPerformed
        camposON();
        botoesOFF();
        txt_codigo.setEnabled(true);
        txt_operacao.setText("Inclusão");
    }//GEN-LAST:event_btn_incluirActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        txt_codigo.setEnabled(false);
        if (tb_pessoas.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "Selecione pelo menos uma Linha da Tabela!");
        } else {
            if (tb_pessoas.getSelectedRowCount() > 1) {
                JOptionPane.showMessageDialog(null, "Selecione apenas uma Linha da Tabela!");
            } else {
                if (tb_pessoas.getSelectedRowCount() == 1) {
                    txt_codigo.setText(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 0).toString());
                    txt_ano.setText(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 5).toString());
                    txt_descricao.setText(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 1).toString());
                    cbx_funcao.setSelectedItem(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 3));
                    cbx_paroquia.setSelectedItem(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 4));
                    txt_encontro.setText(tb_pessoas.getValueAt(tb_pessoas.getSelectedRow(), 6) + "");
                    tb_pessoas.setEnabled(false);
                    camposON();
                    botoesOFF();
                    txt_operacao.setText("Alteração");
                }
            }
        }
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        camposOFF();
        limpaCampos();
        botoesON();
        tb_pessoas.setEnabled(true);
        txt_operacao.setText(null);
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarActionPerformed
        validaCampos();
    }//GEN-LAST:event_btn_salvarActionPerformed

    private void txt_filtroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filtroKeyReleased
        filtrar();
    }//GEN-LAST:event_txt_filtroKeyReleased

    private void txt_codigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_codigoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscaPessoa(txt_codigo.getText());
        }
    }//GEN-LAST:event_txt_codigoKeyPressed

    private void btn_apagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_apagarActionPerformed
        apagar();
    }//GEN-LAST:event_btn_apagarActionPerformed

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
            java.util.logging.Logger.getLogger(Frm_Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm_Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm_Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm_Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_Historico().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_apagar;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_incluir;
    private javax.swing.JButton btn_novaFuncao;
    private javax.swing.JButton btn_novaParoquia;
    private javax.swing.JButton btn_salvar;
    private javax.swing.JComboBox cbx_funcao;
    private javax.swing.JComboBox cbx_paroquia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnl_botoes;
    private javax.swing.JPanel pnl_dados;
    private javax.swing.JPanel pnl_fundo;
    private javax.swing.JTable tb_pessoas;
    private javax.swing.JFormattedTextField txt_ano;
    private javax.swing.JTextField txt_codigo;
    private javax.swing.JTextField txt_descricao;
    private javax.swing.JTextField txt_encontro;
    private javax.swing.JTextField txt_filtro;
    private javax.swing.JTextField txt_operacao;
    private javax.swing.JLabel txt_qtde;
    // End of variables declaration//GEN-END:variables
}
