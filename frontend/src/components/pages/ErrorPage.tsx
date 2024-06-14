import '@mantine/core/styles.css';
import {FC} from "react";
import {useRouteError} from "react-router-dom";
import {Box, Button, Combobox} from "@mantine/core";
import Header = Combobox.Header;

export const ErrorBoundary: FC = () => {
    const err = useRouteError()
    const handleReload = () => {
        window.location.reload()
    }

    if (String(err).includes("dynamically imported")) {
        handleReload()
        return null
    }
    return (
        <Box>
            <Header>エラーが発生しました</Header>
            <Button onClick={handleReload}>
                ページを再読み込みする
            </Button>

        </Box>
    )
}
