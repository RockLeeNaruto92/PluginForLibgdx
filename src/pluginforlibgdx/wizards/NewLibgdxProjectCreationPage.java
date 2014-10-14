package pluginforlibgdx.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewLibgdxProjectCreationPage extends WizardPage {
	private static String WIZARD_PAGE_NAME = "libgdxProjctCreationWizardPage";
	private static String WIZARD_PAGE_TITLE = "Create new libgdx project";
	private static String WIZARD_PAGE_DESCRIPTION = "This wizard creates new libgdx project.";
	
	private static String LABEL_PROJECT_NAME = "Project Name";
	private static String LABEL_PROJECT_MAIN_PACKAGE = "Package";
	private static String LABEL_PROJECT_MAIN_CLASS = "Game class";
	private static String LABEL_PROJECT_DESTINATION_FOLDER = "Destination";
	private static String LABEL_BUTTON_BROWSE = "Browse";
	private static String LABEL_DESKTOP_PROJECT = "Desktop project";
	
	private String DEFAULT_PROJECT_NAME = "NewLibgdxGameProject";
	private String DEFAULT_PROJECT_MAIN_PACKAGE="your.libgdx.project";
	private String DEFAULT_PROJECT_MAIN_CLASS = "GameClass";
	private String DEFAULT_PROJECT_DESTIONATION_FOLDER;
	private boolean isCreateDesktopProject = false;
	
	private Text textProjectNameContainer;
	private Text textProjectMainPackageContainer;
	private Text textProjectMainClassContainer;
	private Text textProjectDestinationFolder;
	private Text containerText;

	private Text fileText;

	private ISelection selection;

	/**
	 * Constructor for NewLibgdxProjectCreationWizardPage.
	 * 
	 * @param pageName
	 */
	public NewLibgdxProjectCreationPage(ISelection selection) {
		super(WIZARD_PAGE_NAME);
		setTitle(WIZARD_PAGE_TITLE);
		setDescription(WIZARD_PAGE_DESCRIPTION);
		
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
		createNameInputText(container);
		createMainPackageInputText(container);
		createMainClassInputText(container);
		createDestinationFolderInputText(container);
		createDesktopVersionCheckbox(container);

		initialize();
		dialogChanged();
		setControl(container);
	}
	
	
	/***
	 * Create name input container include project name label and project name's inputText element
	 * @param container
	 */
	private void createNameInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_NAME);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectNameContainer = new Text(container, SWT.BORDER | SWT.SINGLE);
		textProjectNameContainer.setLayoutData(gd);
		textProjectNameContainer.setText(DEFAULT_PROJECT_NAME);
		textProjectNameContainer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}
	
	/**
	 * Create main package name input container include package name label and project main package name's inputText element
	 * @param container
	 */
	private void createMainPackageInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_MAIN_PACKAGE);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectMainPackageContainer.setLayoutData(gd);
		textProjectMainPackageContainer.setText(DEFAULT_PROJECT_MAIN_PACKAGE);
		textProjectMainPackageContainer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}
	
	
	/**
	 * 
	 * @param container
	 */
	private void createMainClassInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_MAIN_CLASS);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectMainClassContainer.setLayoutData(gd);
		textProjectMainClassContainer.setText(DEFAULT_PROJECT_MAIN_CLASS);
		textProjectMainClassContainer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Button button = new Button(container, SWT.PUSH);
		button.setText(LABEL_BUTTON_BROWSE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
	}
	
	/**
	 * 
	 * @param container
	 */
	private void createDestinationFolderInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_DESTINATION_FOLDER);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectDestinationFolder.setLayoutData(gd);
		textProjectDestinationFolder.setText(DEFAULT_PROJECT_DESTIONATION_FOLDER);
		textProjectDestinationFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
	}
	
	/**
	 * 
	 * @param container
	 */
	private void createDesktopVersionCheckbox(Composite container){
		
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
			}
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getProjectName()));

//		if (getContainerName().length() == 0) {
//			updateStatus("File container must be specified");
//			return;
//		}
//		if (container == null
//				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
//			updateStatus("File container must exist");
//			return;
//		}
//		if (!container.isAccessible()) {
//			updateStatus("Project must be writable");
//			return;
//		}
//		if (fileName.length() == 0) {
//			updateStatus("File name must be specified");
//			return;
//		}
//		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
//			updateStatus("File name must be valid");
//			return;
//		}
//		int dotLoc = fileName.lastIndexOf('.');
//		if (dotLoc != -1) {
//			String ext = fileName.substring(dotLoc + 1);
//			if (ext.equalsIgnoreCase("mpe") == false) {
//				updateStatus("File extension must be \"mpe\"");
//				return;
//			}
//		}
		
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	/**
	 * Check inputed project name
	 */
	boolean isValidProjectName(){
		return true;
	}
	
	boolean isValidProjectMainPackage(){
		return true;
	}
	
	boolean isValidProjectMainClass(){
		return true;
	}
	
	boolean isValidProjectDestinationFolder(){
		return true;
	}
	
	/**
	 * GET method
	 */
	public String getProjectName(){
		return textProjectNameContainer.getText();
	}
	
	public String getProjectMainPackage(){
		return textProjectMainPackageContainer.getText();
	}
	
	public String getProjectMainClass(){
		return textProjectMainClassContainer.getText();
	}
	
	public String getProjectDestinationFolder(){
		return textProjectDestinationFolder.getText();
	}
}