package net.bhpachulski.tddcriteriaserver.menu.dropdown;

import net.bhpachulski.tddcriteriaserver.configuration.TDDCriteriaPluginConfigurationDialog;
import net.bhpachulski.tddcriteriaserver.eclipse.util.EclipseUtil;
import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.project.util.TDDCriteriaProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ConfigProjectAction implements IObjectActionDelegate {
	
	private Shell shell;
	private TDDCriteriaProjectUtil projectPropertiesUtil = new TDDCriteriaProjectUtil();
	private FileUtil futil = new FileUtil();
	
	/**
	 * Constructor for Action1.
	 */
	public ConfigProjectAction() {
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
		TDDCriteriaPluginConfigurationDialog configDialog = new TDDCriteriaPluginConfigurationDialog(shell);
		 configDialog.create();
		 
		if (configDialog.open() == Window.OK) {
			
			IProject currentProject = EclipseUtil.getCurrentProject();
			
			TDDCriteriaProjectProperties propertiesFile = projectPropertiesUtil.verifyProjectProperties(currentProject);
			propertiesFile.setIp(configDialog.getServerIp());
			
			futil.updateProjectConfigFile(currentProject, propertiesFile);
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
