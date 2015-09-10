package net.bhpachulski.tddcriteriaserver.configuration;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TDDCriteriaPluginConfigurationDialog extends TitleAreaDialog {
	
	private Text serverIp;

	private String serverIP;

	public TDDCriteriaPluginConfigurationDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Cnfiguracão - TDD Criteria Plugin");
		setMessage("Nesta tela você pode configurar alguns parâmetros do plugin.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createFirstName(container);

		return area;
	}

	private void createFirstName(Composite container) {
		Label lbServerIP = new Label(container, SWT.NONE);
		lbServerIP.setText("Server IP");

		GridData dataServerIp = new GridData();
		dataServerIp.grabExcessHorizontalSpace = true;
		dataServerIp.horizontalAlignment = GridData.FILL;

		serverIp = new Text(container, SWT.BORDER);
		serverIp.setLayoutData(dataServerIp);
	}

	@Override
	protected boolean isResizable() {
		return false;
	}

	private void saveInput() {
		serverIP = serverIp.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getServerIp() {
		return serverIP;
	}
}
