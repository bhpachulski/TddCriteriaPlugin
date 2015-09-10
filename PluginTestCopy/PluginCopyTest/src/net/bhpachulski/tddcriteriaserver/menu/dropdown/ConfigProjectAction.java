package net.bhpachulski.tddcriteriaserver.menu.dropdown;

import net.bhpachulski.tddcriteriaserver.configuration.TDDCriteriaPluginConfigurationDialog;
import net.bhpachulski.tddcriteriaserver.exception.TDDCriteriaException;
import net.bhpachulski.tddcriteriaserver.project.util.TDDCriteriaProjectUtil;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ConfigProjectAction implements IObjectActionDelegate {
	private Shell shell;
	private TDDCriteriaProjectUtil projectPropertiesUtil = new TDDCriteriaProjectUtil();
	
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
		System.out.println("AQUI !");	
		TDDCriteriaPluginConfigurationDialog dialog = new TDDCriteriaPluginConfigurationDialog(shell);
		dialog.create();
		if (dialog.open() == Window.OK) {
		  System.out.println(dialog.getFirstName());
		  System.out.println(dialog.getLastName());
		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
