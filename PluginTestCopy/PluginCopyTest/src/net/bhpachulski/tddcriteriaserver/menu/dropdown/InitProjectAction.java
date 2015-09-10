package net.bhpachulski.tddcriteriaserver.menu.dropdown;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.bhpachulski.tddcriteriaserver.exception.TDDCriteriaException;
import net.bhpachulski.tddcriteriaserver.project.util.TDDCriteriaProjectUtil;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class InitProjectAction implements IObjectActionDelegate {

	private Shell shell;
	private TDDCriteriaProjectUtil projectPropertiesUtil = new TDDCriteriaProjectUtil();
	
	/**
	 * Constructor for Action1.
	 */
	public InitProjectAction() {
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
			projectPropertiesUtil.verifyProjectProperties(getCurrentProject());
			
			MessageDialog.openInformation(
					shell,
					"TDDCriteria Plugin",
					"O projeto foi inicializado com sucesso.");
			
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
	
	public IProject getCurrentProject () {
		
		IWorkbenchWindow windowI = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStructuredSelection selection = (IStructuredSelection) windowI.getSelectionService().getSelection();
		Object firstElement = selection.getFirstElement();
		IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
		
		return project;
		
	}
	
}
