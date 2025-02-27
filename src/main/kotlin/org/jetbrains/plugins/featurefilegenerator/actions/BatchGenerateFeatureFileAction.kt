package org.jetbrains.plugins.featurefilegenerator.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.Messages
import org.jetbrains.plugins.featurefilegenerator.LLMSettings
import org.jetbrains.plugins.featurefilegenerator.executor.LLMExecutor

class BatchGenerateFeatureFileAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val filePath = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)?.path
            ?: run {
                Messages.showErrorDialog("Não foi possível obter o caminho do arquivo.", "Erro")
                return
            }

        // Usa o LLMSettings do IntelliJ Plugin
        val llmSettings = LLMSettings.getInstance()
        val executor = LLMExecutor(llmSettings)

        executor.executeBatchAsync(filePath) { llmName, result ->
            ApplicationManager.getApplication().invokeLater {
                Messages.showMessageDialog(
                    project,
                    result,
                    "Resultado da Execução ($llmName)",
                    Messages.getInformationIcon()
                )
            }
        }
    }

}
