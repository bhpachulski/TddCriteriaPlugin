package net.bhpachulski.tddcriteriaserver.menu.dropdown;

import net.bhpachulski.tddcriteriaserver.eclipse.util.EclipseUtil;
import net.bhpachulski.tddcriteriaserver.exception.TDDCriteriaException;
import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.network.util.TDDCriteriaNetworkUtil;
import net.bhpachulski.tddcriteriaserver.project.util.TDDCriteriaProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class SendTrackFilesAction implements IObjectActionDelegate {
	
	private Shell shell;
	private TDDCriteriaProjectUtil projectPropertiesUtil = new TDDCriteriaProjectUtil();
	private TDDCriteriaNetworkUtil networkUtil = new TDDCriteriaNetworkUtil();
	private FileUtil futil = new FileUtil();
	
	/**
	 * Constructor for Action1.
	 */
	public SendTrackFilesAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try {
			IProject p = EclipseUtil.getCurrentProject();
			TDDCriteriaProjectProperties tddCriteriaFile = projectPropertiesUtil.verifyProjectProperties(p);
			
			networkUtil.sendAllFiles(tddCriteriaFile, p);
			futil.updateProjectConfigFile(p, tddCriteriaFile);
			
		} catch (TDDCriteriaException e) {
			MessageDialog.openInformation(
					shell,
					"TDDCriteria Plugin",
					"Erro ao inicializar Projeto.");
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
