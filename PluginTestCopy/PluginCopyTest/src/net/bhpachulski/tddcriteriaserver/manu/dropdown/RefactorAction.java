package net.bhpachulski.tddcriteriaserver.manu.dropdown;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

public class RefactorAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public RefactorAction() {
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
		IProject project = getCurrentProject ();
		IPath projectPath = project.getLocation();
		
		List<String> resourceNames = new ArrayList<String>();
		try {
			for (IResource resource : project.members()) {
			    resourceNames.add(resource.getName() + "-" + resource.getFullPath().toString());
			}
		} catch (CoreException e) {
			
			MessageDialog.openInformation(
					shell,
					"PluginCopyTest",
					"Deu pau");

		}

		MessageDialog.openInformation(
			shell,
			"PluginCopyTest",
			"" + resourceNames + " " + projectPath.toOSString());
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
	
	private static void recursiveFindCFiles(ArrayList<IResource> allCFiles, IPath path, IWorkspaceRoot myWorkspaceRoot){
	    IContainer  container =  myWorkspaceRoot.getContainerForLocation(path);

	    try {
	        IResource[] iResources;
	        iResources = container.members();
	        for (IResource iR : iResources){
	            // for c files
	            if ("c".equalsIgnoreCase(iR.getFileExtension()))
	                allCFiles.add(iR);
	            if (iR.getType() == IResource.FOLDER){
	                IPath tempPath = iR.getLocation();
	                recursiveFindCFiles(allCFiles,tempPath,myWorkspaceRoot);
	            }
	        }
	    } catch (CoreException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	public ArrayList<IResource> getAllCFilesInProject() {
	    ArrayList<IResource> allCFiles = new ArrayList<IResource>();
	    IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	    IProject project = getCurrentProject();

	    IPath path = project.getLocation();

	    recursiveFindCFiles(allCFiles,path,myWorkspaceRoot);
	    return allCFiles;
	}

}
