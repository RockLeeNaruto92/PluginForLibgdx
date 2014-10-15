package pluginforlibgdx.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class NewLibgdxProjectCreationWinzard extends Wizard implements
		INewWizard {
	private NewLibgdxProjectCreationPage page;
	private ISelection selection;

	/**
	 * Constructor for NewWizard.
	 */
	public NewLibgdxProjectCreationWinzard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewLibgdxProjectCreationPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		// final String containerName = "container";
		// final String fileName = "examples";
		//
		final String pName = page.getProjectName();
		final String pMainPackage = page.getProjectMainPackage();
		final String pMainClass = page.getProjectMainClass();
		final String pDestinationFolder = page.getProjectDestinationFolder();
		final boolean isCreateDesktopVersion = page.isCreateDesktopProject();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(pName, pMainPackage, pMainClass,
							pDestinationFolder, isCreateDesktopVersion, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(String projectName, String projectMainPackage,
			String projectMainClass, String projectDestinationFolder,
			boolean isCreateDesktopVersion, IProgressMonitor monitor)
			throws CoreException {
		// create project
		System.out.println("do finissh");
		monitor.beginTask("Creating project " + projectName, 2);
		System.out.println("monitor begin task");

		// IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// IResource resource = root
		// .findMember(new Path(projectDestinationFolder));
		//
		// if (!resource.exists() || !(resource instanceof IContainer)) {
		// throwCoreException("Folder \"" + projectDestinationFolder
		// + "\" does not exist.");
		// }

		IProjectDescription description = ResourcesPlugin
				.getWorkspace()
				.loadProjectDescription(
						new Path(
								"/home/superman/commandLineUsing/MyLibgdxGame/.project"));
		System.out.println("load project path");
		System.out.println("descrtion:" + description.getLocationURI());
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(description.getName());
		project.create(description, null);

		// IContainer container = (IContainer) resource;
		// final IFile file = container.getFile(new Path(fileName));
		// try {
		// InputStream stream = openContentStream();
		// if (file.exists()) {
		// file.setContents(stream, true, true, monitor);
		// } else {
		// file.create(stream, true, monitor);
		// }
		// stream.close();
		// } catch (IOException e) {
		// }
		// monitor.worked(1);
		// monitor.setTaskName("Opening file for editing...");
		// getShell().getDisplay().asyncExec(new Runnable() {
		// public void run() {
		// IWorkbenchPage page = PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// try {
		// IDE.openEditor(page, file, true);
		// } catch (PartInitException e) {
		// }
		// }
		// });
		monitor.worked(1);
	}

	// public void importProject(IProgressMonitor monitor) throws CoreException
	// {
	// String name = "project";
	// System.out.println("Create the project : " + name);
	// IProject newProject = ResourcesPlugin.getWorkspace().getRoot()
	// .getProject(name);
	//
	// if (!newProject.exists()) {
	// IProjectDescription description = ResourcesPlugin.getWorkspace()
	// .newProjectDescription(newProject.getName());
	// description.setLocation(location);
	// if (assertExist() && !location.toFile().exists()) {
	// System.out.println("ERROR : " + name + " must exist");
	// } else {
	// newProject.create(description, monitor);
	// newProject.open(monitor);
	// }
	// } else if (!newProject.isOpen()) {
	// newProject.open(monitor);
	// }
	// }

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents = "This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "PluginForLibgdx",
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}