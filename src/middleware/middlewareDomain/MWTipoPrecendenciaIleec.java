package middleware.middlewareDomain;

public class MWTipoPrecendenciaIleec extends MWDomainObject
{
    private String descricao;
    private Integer tipoPrecedencia;

    public String getDescricao()
    {
        return this.descricao;
    }

    public void setDescricao(String param)
    {
        this.descricao = param;
    }

    public Integer getTipoPrecedencia()
    {
        return this.tipoPrecedencia;
    }
    public void setTipoPrecedencia(Integer param)
    {
        this.tipoPrecedencia = param;
    }

    public String toString()
    {
        return " [descricao] " + descricao + " [tipo_precedencia] " + tipoPrecedencia;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof MWTipoPrecendenciaIleec)
        {
            MWTipoPrecendenciaIleec mwtpi = (MWTipoPrecendenciaIleec) obj;
            result = getTipoPrecedencia().equals(mwtpi.getTipoPrecedencia()) && getDescricao().equals(mwtpi.getDescricao());
            }
        return result;
    }
}