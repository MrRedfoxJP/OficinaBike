package br.com.oficinabike.util;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Messages;

import br.com.oficinabike.bean.AutenticacaoBean;
import br.com.oficinabike.domain.Usuario;

@SuppressWarnings("serial")
public class AutenticacaoPhaseListener implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		UIViewRoot uiViewRoote = facesContext.getViewRoot();
		String paginaAtual = uiViewRoote.getViewId();

		boolean epaginaautenticacao = paginaAtual.contains("Autenticacao.xhtml");		

		if (!epaginaautenticacao) {
			ExternalContext externalContext = facesContext.getExternalContext();
			Map<String, Object> mapa = externalContext.getSessionMap();
			AutenticacaoBean autenticacaoBean = (AutenticacaoBean) mapa.get("autenticacaoBean");
			Usuario user = autenticacaoBean.getUsuario();

			if (user.getTipo() == null) {
				Messages.addGlobalError("Funcionario não autenticado");

				Application application = facesContext.getApplication();
				NavigationHandler navigationHandler = application.getNavigationHandler();
				navigationHandler.handleNavigation(facesContext, null,
						"/paginas/Autenticacao.xhtml?faces-redirect=true");

			}
		}		
	}

	@Override
	public void beforePhase(PhaseEvent event) {

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
