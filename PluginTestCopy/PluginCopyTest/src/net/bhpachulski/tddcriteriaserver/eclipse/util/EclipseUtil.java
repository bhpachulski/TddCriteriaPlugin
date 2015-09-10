package net.bhpachulski.tddcriteriaserver.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class EclipseUtil {

public static IProject getCurrentProject () {
		
		IWorkbenchWindow windowI = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStructuredSelection selection = (IStructuredSelection) windowI.getSelectionService().getSelection();
		Object firstElement = selection.getFirstElement();
		IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
		
		return project;
		
	}
	
}
