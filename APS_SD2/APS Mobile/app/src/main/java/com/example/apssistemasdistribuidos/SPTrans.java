package com.example.apssistemasdistribuidos;

import java.util.ArrayList;

public class SPTrans {
    //buscarLinha
    private int identificadorLinha;
    private boolean modoCircular;
    private  String letreiro1;
    private int letreiro2;
    private int sentidoLinha;
    private String terminalPrincipal;
    private String terminalSecundario;

    public int getIdentificadorLinha() {
        return identificadorLinha;
    }

    public boolean isModoCircular() {
        return modoCircular;
    }

    public String getLetreiro1() {
        return letreiro1;
    }

    public int getLetreiro2() {
        return letreiro2;
    }

    public int getSentidoLinha() {
        return sentidoLinha;
    }

    public String getTerminalPrincipal() {
        return terminalPrincipal;
    }

    public String getTerminalSecundario() {
        return terminalSecundario;
    }

    public int getIdentificadorParada() {
        return identificadorParada;
    }

    public String getNomeParada() {
        return nomeParada;
    }

    public String getEnderecoParada() {
        return enderecoParada;
    }

    public double getLatitudeParada() {
        return latitudeParada;
    }

    public double getLongitudeParada() {
        return longitudeParada;
    }

    public String getHorario() {
        return horario;
    }

    public ArrayList<SPTrans.linhasLocalizadas> getLinhasLocalizadas() {
        return linhasLocalizadas;
    }

    //construtor para o buscarLinhas
    public SPTrans(int identificadorLinha, boolean modoCircular, String letreiro1, int letreiro2, int sentidoLinha, String terminalPrincipal, String terminalSecundario) {
        this.identificadorLinha = identificadorLinha;
        this.modoCircular = modoCircular;
        this.letreiro1 = letreiro1;
        this.letreiro2 = letreiro2;
        this.sentidoLinha = sentidoLinha;
        this.terminalPrincipal = terminalPrincipal;
        this.terminalSecundario = terminalSecundario;
    }


    //construtor para buscarParadas
    public SPTrans(int identificadorParada, String nomeParada, String enderecoParada, double latitudeParada, double longitudeParada) {
        this.identificadorParada = identificadorParada;
        this.nomeParada = nomeParada;
        this.enderecoParada = enderecoParada;
        this.latitudeParada = latitudeParada;
        this.longitudeParada = longitudeParada;
    }

    //construtor para retornaPosicao
    public SPTrans(String horario, ArrayList<SPTrans.linhasLocalizadas> linhasLocalizadas) {
        this.horario = horario;
        this.linhasLocalizadas = linhasLocalizadas;
    }

    //buscarParada
    private  int identificadorParada;
    private  String nomeParada;
    private  String enderecoParada;
    private double latitudeParada;
    private  double longitudeParada;

    //retornaPosicao
    String horario;
    ArrayList<linhasLocalizadas> linhasLocalizadas = new ArrayList<>();
    //sequencia de posiçoes do array
    static class linhasLocalizadas{
        private String letreiroCompleto;
        private int identificadorLinha;
        private int sentidoLinha; //se tiver 1 significa que está indo do terminal principal para o secundário. Se estiver 2 é o contrário
        private String letreiroDestino;
        private String letreiroOrigem;
        private int quantidadeVeiculos;
        ArrayList<relacaoVeiculos> relacaoVeiculos = new ArrayList<>();

        public String getLetreiroCompleto() {
            return letreiroCompleto;
        }

        public int getIdentificadorLinha() {
            return identificadorLinha;
        }

        public int getSentidoLinha() {
            return sentidoLinha;
        }

        public String getLetreiroDestino() {
            return letreiroDestino;
        }

        public String getLetreiroOrigem() {
            return letreiroOrigem;
        }

        public int getQuantidadeVeiculos() {
            return quantidadeVeiculos;
        }

        public ArrayList<relacaoVeiculos> getRelacaoVeiculos() {
            return relacaoVeiculos;
        }

        static class relacaoVeiculos{
            private int prefixoVeiculo;
            private boolean acessivelDeficientes;
            String horarioUTC;
            private double latitudeVeiculo;
            private  double longitudeVeiculo;

            public int getPrefixoVeiculo() {
                return prefixoVeiculo;
            }

            public boolean isAcessivelDeficientes() {
                return acessivelDeficientes;
            }

            public String getHorarioUTC() {
                return horarioUTC;
            }

            public double getLatitudeVeiculo() {
                return latitudeVeiculo;
            }

            public double getLongitudeVeiculo() {
                return longitudeVeiculo;
            }

            //construtor relacaoVeiculos -- ele é full
            public relacaoVeiculos(int prefixoVeiculo, boolean acessivelDeficientes, String horarioUTC, double latitudeVeiculo, double longitudeVeiculo) {
                this.prefixoVeiculo = prefixoVeiculo;
                this.acessivelDeficientes = acessivelDeficientes;
                this.horarioUTC = horarioUTC;
                this.latitudeVeiculo = latitudeVeiculo;
                this.longitudeVeiculo = longitudeVeiculo;


            }
        }


        //construtor linhasLocalidas -- ele é full
        public linhasLocalizadas(String letreiroCompleto, int identificadorLinha, int sentidoLinha, String letreiroDestino, String letreiroOrigem, int quantidadeVeiculos, ArrayList<relacaoVeiculos> relacaoVeiculos) {
            this.letreiroCompleto = letreiroCompleto;
            this.identificadorLinha = identificadorLinha;
            this.sentidoLinha = sentidoLinha;
            this.letreiroDestino = letreiroDestino;
            this.letreiroOrigem = letreiroOrigem;
            this.quantidadeVeiculos = quantidadeVeiculos;
            this.relacaoVeiculos = relacaoVeiculos;
        }
    }






}
