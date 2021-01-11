package br.com.oficinabike.bean;

import java.io.InputStream;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.oficinabike.domain.ItemVenda;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RelatorioBean {
	public void gerarRelatorio(List<ItemVenda> itensVenda) throws JRException {
		// abre relatorio para impressão --- não utilizado
		InputStream fonte = RelatorioBean.class.getResourceAsStream("/reports/orcamento.jrxml");
		JasperReport report = JasperCompileManager.compileReport(fonte);
		JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(itensVenda));
		JasperPrintManager.printReport(print, true);

	}

	public void visualizarRelatorio(List<ItemVenda> itensVenda) {

		try {

			// abre o relatório em outra aba
			System.out.println("entrou no visualizar relatorio");

			// ---------- gera o relatorio ----------
			InputStream fonte = RelatorioBean.class.getResourceAsStream("/reports/orcamento.jrxml");
			JasperReport report = JasperCompileManager.compileReport(fonte);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(itensVenda));
			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			res.setContentType("application/pdf");
			// Código abaixo gerar o relatório e disponibiliza diretamente
			// na página
			res.setHeader("Content-disposition", "inline;filename=arquivo.pdf");
			// Código abaixo gerar o relatório e disponibiliza para o
			// cliente baixar ou salvar
			// res.setHeader("Content-disposition",
			// "attachment;filename=arquivo.pdf");
			res.getOutputStream().write(b);
			res.getCharacterEncoding();
			FacesContext.getCurrentInstance().responseComplete();
			System.out.println("saiu do visualizar relatorio");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
